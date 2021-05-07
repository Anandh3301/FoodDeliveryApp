package com.capgemini.deliveryapp.view.deliveryactivity.slidemenu.orders


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capgemini.deliveryapp.R
import com.capgemini.firebasedemo.AppData.Menu.Orders
import java.text.SimpleDateFormat
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyOrderRecyclerViewAdapter(
    private val values: List<Orders>
) : RecyclerView.Adapter<MyOrderRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = values[position]
        Log.d("hisotry", order.toString())

        holder.ocostT.text = "₹" + order.total.toString()
        if (order.order.length > 70)
            order.order = order.order.substring(0, 70) + "..."

        holder.oorderT.text = order.order

        holder.oresT.text = order.location
        //   val sfd = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        //   val x= sfd.format(Date(order.date.toString()))
        // Log.d("dateeee",x)
        val y: Date = order.date.toDate()
        Log.d("dateeee", y.toString())
        val format1 = SimpleDateFormat("dd-MM-yyyy   HH:mm")
        holder.odateT.text = format1.format(y)

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val odateT = view.findViewById<TextView>(R.id.odateT)
        val ocostT = view.findViewById<TextView>(R.id.ocostT)
        val oorderT = view.findViewById<TextView>(R.id.oorderT)
        val oresT = view.findViewById<TextView>(R.id.oresT)

    }
}