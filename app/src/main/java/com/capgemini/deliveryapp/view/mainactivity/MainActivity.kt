package com.capgemini.deliveryapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.capgemini.deliveryapp.R
import com.capgemini.deliveryapp.Repository.DBWrapper
import com.capgemini.firebasedemo.AppData.FireBaseWrapper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      //This code needs to be in map fragment(april 28 16:45
        val wrapper= FireBaseWrapper()


        wrapper.getLocationsOfCity("bangalore")//pass the city of user here
        {
            //it is a list of locations in that city.Add markers to these locations
            Log.d("pickedcity",it.toString())
            //it.foreach(add markers)
        }
    }
}