package com.capgemini.deliveryapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.capgemini.deliveryapp.R
import com.capgemini.firebasedemo.AppData.FireBaseWrapper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    var lat: Double = 0.0
    var lng: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lat = it.getDouble("lat")
            lng = it.getDouble("lng")
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        //13.0271074,77.6650655
        //13.0213918,77.6583065
        //13.0158587,77.6568631
        val cityname = LatLng(lat, lng)
        val rest1 = LatLng(13.0271074, 77.6650655)
        val rest2 = LatLng(13.0213918, 77.6583065)
        val rest3 = LatLng(13.0158587, 77.6568631)
        val zoom = CameraUpdateFactory.zoomTo(10f)
        val marker = googleMap.addMarker(MarkerOptions().position(cityname).title("Your city"))
        marker.showInfoWindow()
        googleMap.addMarker(MarkerOptions().position(rest1).title("RESTAURANT1")).showInfoWindow()
        googleMap.addMarker(MarkerOptions().position(rest2).title("RESTAURANT2")).showInfoWindow()
        googleMap.addMarker(MarkerOptions().position(rest3).title("RESTAURANT3")).showInfoWindow()
        // firebase call. Pass the city of user and returns a list of locations from that city
        val wrapper=FireBaseWrapper()
        wrapper.getLocationsOfCity("bangalore"){

            Log.d("citymarkers",it.toString())
            //add markers here || it is a List<Location> which has address,lat,lng
        }

//move camera to central bangalore
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(cityname))
        googleMap.animateCamera(zoom)
        googleMap.setOnMarkerClickListener {
            val textview = view?.findViewById<TextView>(R.id.maptext)
            when (it.title) {
                "RESTAURANT1" -> {
                    Toast.makeText(context, "REST1", Toast.LENGTH_LONG).show()
                    textview?.setText("RESTAURANT1")
                    goToRegister()

                }
                "RESTAURANT2" -> {
                    Toast.makeText(context, "REST2", Toast.LENGTH_LONG).show()
                    textview?.setText("RESTAURANT2")
                    goToRegister()
                }
                "RESTAURANT3" -> {
                    Toast.makeText(context, "REST3", Toast.LENGTH_LONG).show()
                    textview?.setText("RESTAURANT3")
                    goToRegister()
                }


            }
            true

        }


    }

    private fun goToRegister() {
        findNavController().navigate(R.id.action_mapsFragment_to_registerFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}