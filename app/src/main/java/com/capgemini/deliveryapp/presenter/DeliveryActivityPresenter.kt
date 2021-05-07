package com.capgemini.deliveryapp.presenter

import android.content.SharedPreferences
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.capgemini.deliveryapp.R
import com.capgemini.deliveryapp.view.deliveryactivity.MyBottomSheetDialogFragment
import com.capgemini.firebasedemo.AppData.FireBaseWrapper
import com.google.firebase.auth.FirebaseAuth

class DeliveryActivityPresenter(val view: DeliveryView) {

    //get the name of user with current UID and update slidemenu with the name
    fun getNameFromFauth() {
        val fAuth = FirebaseAuth.getInstance()
        if (fAuth.currentUser != null) {
            Log.d("session", fAuth.currentUser.getIdToken(false).toString())
        }
        val wrapper = FireBaseWrapper()
        wrapper.getUserFromId(fAuth.currentUser.uid) {
            Log.d("NameOfUSer", it.name)
            view.updatenameText(it.name)
            val addr = getSharedPrefLocation()
            view.updateAddressText("Tangito- " + addr)
        }
    }

    //bring up bottom sheet when floating action button is clicked
    fun showBottomSheet() {
        val fAuth = FirebaseAuth.getInstance()
        val wrapper = FireBaseWrapper()
        val x = wrapper.getPreviousOrdersById(fAuth.currentUser.uid) {

        }
        Log.d("previousorders", x.toString())
        Log.d("bottm", "showing bottom sheet from presenter")
        val fmanager = view.getfromSupportFragmentManager()
        MyBottomSheetDialogFragment().apply {
            show(fmanager, tag)
        }


    }

    // get sharedpref location(address of restaurant) and update to the slidemenu
    fun getSharedPrefLocation(): String {
        val pref = view.gettheSharedPreferences()
        val user = pref?.getString("location", "") ?: ""
        return user

    }


    interface DeliveryView {
        fun getfromSupportFragmentManager(): FragmentManager

        fun updatenameText(name: String)
        fun updateAddressText(address: String)

        fun gettheSharedPreferences(): SharedPreferences?

    }

}