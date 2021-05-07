package com.capgemini.deliveryapp.view.deliveryactivity.maps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.capgemini.deliveryapp.R
import com.capgemini.firebasedemo.AppData.FireBaseWrapper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.capgemini.deliveryapp.view.deliveryactivity.DeliveryActivity

// april 29 8 pm
class MapsFragment : Fragment() {
    val PREF_NAME = "Credentials"
    var lat: Double = 0.0

    var lng: Double = 0.0
    var city = " "

    //check if internet is on.If Off, run infinite alertdialog loop
    override fun onResume() {
        super.onResume()
        (activity as DeliveryActivity).checkInternetConnectivity()
    }
    //retrieve data from bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lat = it.getDouble("lat")
            lng = it.getDouble("lng")
            city = it.getString("Cityname", " ")

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

        val userLoc = LatLng(lat, lng)
        //zoom to marker location
        val zoom = CameraUpdateFactory.zoomTo(11f)
        val wrapper = FireBaseWrapper()
        Log.d("isitworking", city.toLowerCase())
        wrapper.getLocationsOfCity(city.toLowerCase()) {
            if (it.isEmpty()) {
                Toast.makeText(context, "Sorry! We are not available here", Toast.LENGTH_LONG)
                    .show()
            } else {
                Log.d("citymarkers", it.size.toString())
                //populate map with blue markers from FireBase
                for (i in 0..it.size - 1) {
                    googleMap.addMarker(
                        MarkerOptions().position(LatLng(it[i].lat, it[i].lng)).title(it[i].address)
                            .snippet("tap to select")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    )
                }

                Log.d("citymarkers", it.toString())
                //add markers here || it is a List<Location> which has address,lat,lng
            }
        }
        // add green marker to user location
        val marker = googleMap.addMarker(
            MarkerOptions().position(userLoc).title("Your are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )
        marker.showInfoWindow()

        //zoom to user location
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLoc))
        googleMap.animateCamera(zoom)
        //set click listener which opens showInfoWindow
        googleMap.setOnMarkerClickListener {
            it.showInfoWindow()

            Toast.makeText(context, it.title, Toast.LENGTH_LONG).show()
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it.position))


            true

        }
        googleMap.setOnInfoWindowClickListener {
            //save to sharedpreferences
            if (it.title.equals("Your are here")) {
                //do nothing if user marker is selected
            } else {   //navigate to restaurant
                saveAddress(it.title)
                //this code from activity runs to update slidemenu directly
                (activity as DeliveryActivity).updateADdressTextView(getString(R.string.app_name) + "- " + it.title)
                goToMenu()
            }
        }


    }

    private fun goToMenu() {
        findNavController().navigate(R.id.action_mapsFragment_to_nav_menu)
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
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.menu_detail)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    //save the address of user selected location to the sharedpreferences
    private fun saveAddress(address: String) {
        //getShared
        val pref = context?.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        val editor = pref?.edit()
        if (editor != null) {
            editor.putString("location", address)

            editor.commit() //apply() for asynchronous commit
        }


    }

}