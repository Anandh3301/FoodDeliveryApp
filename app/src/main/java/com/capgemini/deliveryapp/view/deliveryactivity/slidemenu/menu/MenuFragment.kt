package com.capgemini.deliveryapp.view.deliveryactivity.slidemenu.menu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capgemini.deliveryapp.R
import com.capgemini.deliveryapp.view.deliveryactivity.DeliveryActivity
import com.capgemini.firebasedemo.AppData.FireBaseWrapper

/**
 * A fragment representing a list of Items.
 */
class MenuFragment : Fragment() {

    private var columnCount = 1
    override fun onResume() {
        super.onResume()
        (activity as DeliveryActivity).checkInternetConnectivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("menu created", "menu called")
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                val wrapper = FireBaseWrapper()
                wrapper.getMenu {

                    adapter = MyMenuRecyclerViewAdapter(it, activity)
                }

            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}