package com.capgemini.deliveryapp.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.capgemini.deliveryapp.R
import kotlinx.android.synthetic.main.fragment_send_button.*


class sendButton : Fragment() {
    lateinit var lmanager: LocationManager
    var currentloc: Location?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_button, container, false)
    }
    private fun getCityName(): String {
        val gCoder= Geocoder(activity)
        val addList=gCoder.getFromLocation(currentloc?.latitude!!,currentloc?.longitude!!,10)
        if(addList.isNotEmpty()){
            val addr=addList[0]
            var strAddr=""
            strAddr=addr.locality
            return strAddr
        }
        else{
            return ""}
    }
    private fun updateLocation(loc: Location) {
        val latt=loc.latitude
        val longi=loc.longitude
        var dist=0f
        if(currentloc!=null){
            dist= currentloc?.distanceTo(loc)!!
        }
        currentloc = loc
        val city=getCityName()
        cityName.setText(city)
    }
    private fun checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if( ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION),1)
            }
            else{
                Toast.makeText(context,"Location permission granted..",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        lmanager= activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //locatiuon provider
        val providerlist=lmanager.getProviders(true)
        var providername:String=""
        if (providerlist.isNotEmpty()) {
            if (providerlist.contains(LocationManager.GPS_PROVIDER)) {
                providername = LocationManager.GPS_PROVIDER
            } else if (providerlist.contains(LocationManager.NETWORK_PROVIDER)) {
                providername = LocationManager.NETWORK_PROVIDER

            } else
                providername = providerlist[0]


            val loc = lmanager.getLastKnownLocation(providername)




            if (loc != null) {
                updateLocation(loc)
            } else
                Toast.makeText(context, "No location found", Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(context,"Some major error", Toast.LENGTH_LONG).show()
        }
        val button=view.findViewById<Button>(R.id.buttonLatlng)

       button.setOnClickListener {
            val bundle= Bundle()
            bundle.putDouble("lat",currentloc?.latitude!!)
            bundle.putDouble("lng",currentloc?.longitude!!)
            findNavController().navigate(R.id.action_sendButton_to_mapsFragment,bundle)
        }
    }


}