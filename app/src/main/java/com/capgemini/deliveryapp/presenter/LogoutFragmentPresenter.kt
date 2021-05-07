package com.capgemini.deliveryapp.presenter

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LogoutFragmentPresenter(val view: LogoutView) {
    //sign out the current user on logout
    fun signoutFireBase() {
        val fAuth = FirebaseAuth.getInstance()
        fAuth.signOut()
    }

    //delete the sharedpref Location which is stored for the current user
    fun deleteSharedPrefLocation() {
        val pref = view.gettheSharedPreferences()

        if (pref != null) {
            pref.edit().remove("location").commit()
        }


    }


    interface LogoutView {

        fun gettheSharedPreferences(): SharedPreferences?
        fun navigateToHomeScreen()
    }
}