package com.capgemini.deliveryapp.view.deliveryactivity.slidemenu.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.capgemini.deliveryapp.R
import com.capgemini.deliveryapp.view.deliveryactivity.DeliveryActivity
import com.capgemini.firebasedemo.AppData.FireBaseWrapper
import com.google.firebase.auth.FirebaseAuth


/**
 * A fragment representing a list of Items.
 */
class OrderFragment : Fragment() {

    private var columnCount = 1
    override fun onResume() {
        super.onResume()
        (activity as DeliveryActivity).checkInternetConnectivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                val fAuth = FirebaseAuth.getInstance()
                val wrapper = FireBaseWrapper()
                wrapper.getPreviousOrdersById(fAuth.currentUser.uid) {
                    adapter = MyOrderRecyclerViewAdapter(it)
                }
                //    adapter = MyOrderRecyclerViewAdapter(DummyContent.ITEMS)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.send_button)
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}