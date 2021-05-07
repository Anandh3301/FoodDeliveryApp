package com.capgemini.deliveryapp.view.deliveryactivity.slidemenu.menu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.capgemini.deliveryapp.R
import com.capgemini.deliveryapp.Repository.DBWrapper
import com.capgemini.deliveryapp.view.deliveryactivity.DeliveryActivity
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_menu_details.*


private const val ARG_PARAM1 = "title"
private const val ARG_PARAM2 = "description"
private const val ARG_PARAM3 = "tag"
private const val ARG_PARAM4 = "image"

class MenuDetailsFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var title: String? = null
    private var description: String? = null
    private var itemtag: String? = null
    private var image: String? = null

    //retrieving data from bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_PARAM1)
            description = it.getString(ARG_PARAM2)
            itemtag = it.getString(ARG_PARAM3)
            image = it.getString(ARG_PARAM4)

        }
        Log.d("descriptionfrag", title!!)
        Log.d("descriptionfrag", description!!)
        Log.d("descriptionfrag", itemtag!!)
        Log.d("descriptionfrag", image!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_details, container, false)
    }


    //set all the views in deetailfragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.menu_detail)
        Glide.with(context).load(image).into(dIV)
        dnameT.text = title
        ddetailsT.text = description
        val wrapper = context?.let { DBWrapper(it) }
        var quantity = wrapper?.getQuantityofTag(itemtag!!)!!
        val price = wrapper?.getPriceofTag(itemtag!!)
        Log.d("Cost", "$price : $quantity")
        dcountE.text = quantity.toString()
        dpriceT.text = (quantity!! * price!!).toString()

//on minus click,decrement quantity
        minusB.setOnClickListener {

            if (quantity > 0) {
                quantity = quantity - 1


                dcountE.text = quantity.toString()
                dpriceT.text = (quantity!! * price!!).toString()


            }

        }
        //on pluss click,increment quantity
        plusB.setOnClickListener {

            quantity = quantity + 1
            dcountE.text = quantity.toString()
            dpriceT.text = "₹" + (quantity!! * price!!).toString()

        }
        //add items to cart on thiss button click .Pop off the backstack and update DB
        dcartB.setOnClickListener {

            wrapper.updateQuantity(itemtag!!, quantity)
            Toast.makeText(requireContext(), getString(R.string.addcart), Toast.LENGTH_SHORT).show()
            val badge = (activity as AppCompatActivity).findViewById<TextView>(R.id.badgeT)
            badge.text = wrapper.getTotalQuantity().toString()

            findNavController().popBackStack()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        // TODO: Customize parameter argument names

        const val ARG_PARAM1 = "title"
        const val ARG_PARAM2 = "description"
        const val ARG_PARAM3 = "tag"
        const val ARG_PARAM4 = "image"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(title: String, description: String, tag: String, image: String) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    // putInt(ARG_COLUMN_COUNT, columnCount)
                    putString(ARG_PARAM1, title)
                    putString(ARG_PARAM2, description)
                    putString(ARG_PARAM3, tag)
                    putString(ARG_PARAM4, image)
                }
            }
    }


}