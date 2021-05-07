package com.capgemini.deliveryapp.view.deliveryactivity.slidemenu.menu

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.capgemini.deliveryapp.R


import com.capgemini.firebasedemo.AppData.Menu.Item
import com.google.firebase.storage.FirebaseStorage

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyMenuRecyclerViewAdapter(
    private val values: List<Item>,
    private val context: FragmentActivity?
) : RecyclerView.Adapter<MyMenuRecyclerViewAdapter.ViewHolder>() {
    // populate each view with relevant data
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val detailsB = view.findViewById<Button>(R.id.detailB)
        val foodT = view.findViewById<TextView>(R.id.foodT)
        val priceT = view.findViewById<TextView>(R.id.priceT)
        val imageT = view.findViewById<ImageView>(R.id.imageT)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_menu, parent, false)
        return ViewHolder(view)
    }

    //(activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.menu_detail)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.foodT.text = item.item
        holder.priceT.text = "₹" + item.price.toString()


        Log.d("glide", item.image)
        // get picture from firebase storage
        Glide.with(context)
            .load(item.image)
            .into(holder.imageT)
        //when order is clicked,open details fragment
        holder.detailsB.setOnClickListener {
            //add data from current view and send to details fragment
            val bundle = Bundle()
            bundle.putString("title", item.item)
            bundle.putString("description", item.description)
            bundle.putString("tag", item.tag)
            bundle.putString("image", item.image)

            holder.detailsB.findNavController()
                .navigate(R.id.action_nav_menu_to_menuDetailsFragment, bundle)
        }
    }

    override fun getItemCount(): Int = values.size
}