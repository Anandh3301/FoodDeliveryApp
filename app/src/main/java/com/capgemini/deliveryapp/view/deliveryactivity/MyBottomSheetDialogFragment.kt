package com.capgemini.deliveryapp.view.deliveryactivity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.capgemini.deliveryapp.R
import com.capgemini.deliveryapp.Repository.DBWrapper
import com.capgemini.deliveryapp.view.checkoutactivity.CheckoutActivity
import com.capgemini.firebasedemo.AppData.Menu.Food
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_my_bottom_sheet_dialog.*


class MyBottomSheetDialogFragment : BottomSheetDialogFragment() {
    lateinit var foodlist: ArrayList<Food>
    lateinit var mbottomsheet: TextView
    val PREF_NAME = "Credentials"

    override fun onResume() {
        super.onResume()
        (activity as DeliveryActivity).checkInternetConnectivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_my_bottom_sheet_dialog, container, false)
    }


    override fun onPause() {
        super.onPause()
        (activity as DeliveryActivity).updateCartBadge()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val wrapper = DBWrapper(requireContext())
        foodlist = wrapper.retrieveCartAsList()

        Log.d("foods", foodlist.toString())

        //populating the list
        bottomsheettotal.text = "₹${wrapper.calculateBill()}"
        mbottomsheet = bottomsheettotal
        listViewOptions.adapter = MyAdapter(
            requireContext(),

            foodlist,
            view
        )
        checkoutBtn.setOnClickListener {

            if (wrapper.calculateBill() == 0)
                Toast.makeText(requireContext(), getString(R.string.emptycart), Toast.LENGTH_LONG).show()
            else if (getSharedPrefLocation(requireContext()).isNullOrEmpty())
                Toast.makeText(requireContext(), getString(R.string.selectrestaurant), Toast.LENGTH_LONG).show()
            else {
                this.dismiss()
                val intent = Intent(activity, CheckoutActivity::class.java)
                startActivity(intent)

            }
        }
        listViewOptions.setOnItemClickListener { parent, view, position, id ->

            Log.d("itemclick", position.toString())
            Log.d("itemclick", foodlist[position].name + " " + foodlist[position].tag)


        }

    }

    class MyAdapter(
        private val context: Context,
        private val arrayList: java.util.ArrayList<Food>,
        private val view: View?
    ) : BaseAdapter() {

        private lateinit var name: TextView

        private lateinit var quant: TextView
        private lateinit var price: TextView
        private lateinit var cartprice: TextView
        private lateinit var cartquant: TextView
        private lateinit var plusbutton1: ImageButton
        private lateinit var minusbutton1: ImageButton
        override fun getCount(): Int {
            return arrayList.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val wrapper = DBWrapper(context)
            var convertView =
                LayoutInflater.from(context).inflate(R.layout.bottomsheetrow, parent, false)
            //update the views of listview with relevant data
            minusbutton1 = convertView.findViewById(R.id.cartminusb)
            plusbutton1 = convertView.findViewById(R.id.cartplusb)
            cartprice = convertView.findViewById(R.id.cartpricetxt)
            cartquant = convertView.findViewById(R.id.cartquanttxt)
            name = convertView.findViewById(R.id.bottomname)
            quant = convertView.findViewById(R.id.cartquanttxt)
            price = convertView.findViewById(R.id.cartpricetxt)
            name.text = arrayList[position].name
            price.text = "₹" + arrayList[position].totalprice
            quant.text = arrayList[position].quantity.toString()
            //dynamically update/decrement CART on minus and reflect the changes real time
            minusbutton1.setOnClickListener {
                Log.d("itemclick", "minus clicked")
                if (wrapper.getQuantityofTag(arrayList[position].tag) > 0) {
                    wrapper.decrementQuantity(arrayList[position].tag)
                    cartquant.text = wrapper.getQuantityofTag(arrayList[position].tag).toString()

                    if (view != null) {
                        val x = view.findViewById<TextView>(R.id.bottomsheettotal)
                        x.text = "₹" + wrapper.calculateBill().toString()
                    }
                    arrayList[position].quantity--
                    notifyDataSetChanged()
                }

            }
            //dynamically update/increment CART on plus and reflect the changes real time
            plusbutton1.setOnClickListener {
                Log.d("itemclick", "plus clicked")
                wrapper.incrementQuantity(arrayList[position].tag)
                cartquant.text = wrapper.getQuantityofTag(arrayList[position].tag).toString()

                if (view != null) {
                    val x = view.findViewById<TextView>(R.id.bottomsheettotal)
                    x.text = "₹" + wrapper.calculateBill().toString()
                    arrayList[position].quantity++

                    notifyDataSetChanged()

                }

            }
            return convertView
        }
    }

    //get address
    fun getSharedPrefLocation(context: Context): String {
        val pref = context.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        val user = pref.getString("location", "")
        return user!!

    }


}
