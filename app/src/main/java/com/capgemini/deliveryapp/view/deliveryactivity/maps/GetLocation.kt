package com.capgemini.deliveryapp.view.deliveryactivity.maps
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.location.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.capgemini.deliveryapp.R
import com.capgemini.deliveryapp.view.deliveryactivity.DeliveryActivity
import com.capgemini.firebasedemo.AppData.FireBaseWrapper
import kotlinx.android.synthetic.main.fragment_send_button.*
import java.util.*

class sendButton : Fragment() {

    //Declaring the needed Variables
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    val PERMISSION_ID = 1010
    val wrapper=FireBaseWrapper()
    override fun onResume() {
        super.onResume()
        getLastLocation()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_button, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.send_button)
        super.onViewCreated(view, savedInstanceState)
    }
    private fun getCityName(lat: Double,long: Double):String{
        var cityName:String = ""
        var countryName = ""
        var geoCoder = Geocoder(activity, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,3)
        cityName = Adress.get(0).locality ?: getString(R.string.oops)
        countryName = Adress.get(0).countryName  ?:getString(R.string.notfound)
        Log.d("Debug:","Your City: " + cityName + " ; your Country " + countryName)
        return cityName
    }

    fun isLocationEnabled(): Boolean {
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug:", "You have the Permission")
            }
        }
    }
    fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                activity?.parent!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        NewLocationData()
                    } else {
                        Log.d("Debug:", "Your Location:" + location.longitude)
                        val city =getCityName(location.latitude,location.longitude)
                        cityName.text = city
                        buttonLatlng.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putDouble("lat", location.latitude)
                            bundle.putDouble("lng", location.longitude)
                            bundle.putString("Cityname",city)

                            wrapper.getListOfCities {
                                Log.d("containedcity","${city}")
                                if( it.contains(city.toLowerCase())) {
                                    Log.d("containedcity", "${city.toLowerCase()}")
                                    findNavController().navigate(R.id.action_sendButton_to_mapsFragment, bundle)

                                }
                                else
                                {
                                    findNavController().navigate(R.id.action_sendButton_to_oopsFragment2)
                                }
                            }
                          //  findNavController().navigate(R.id.action_sendButton_to_oopsFragment2)



                        }
                    }
                }
            } else {
                Toast.makeText(activity, "Please Turn on Your device Location", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText( activity,"No location given",Toast.LENGTH_SHORT).show()
        }
    }
    private fun NewLocationData() {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity?.parent!!)
        if (ActivityCompat.checkSelfPermission(
                activity?.parent!!,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                activity?.parent!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper()
            )
        }
    }
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:", "your last last location: " + lastLocation.longitude.toString())
            val city =getCityName(lastLocation.latitude,lastLocation.longitude)
            cityName.text =
                "You Last Location is : \n"+city
            val bundle = Bundle()
            bundle.putDouble("lat", lastLocation.latitude)
            bundle.putDouble("lng", lastLocation.longitude)
            bundle.putString("Cityname",city)
            findNavController().navigate(R.id.action_sendButton_to_mapsFragment, bundle)
        }
    }
}