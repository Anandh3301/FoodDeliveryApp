package com.capgemini.deliveryapp.presenter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat

class MainActivityPresenter(val view1: MainView) {
    // Code checks for Location Permission, If permission doesnt exist,request it
    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view1.checkSelfPermission()
            ) {

                view1.requestPermissions()
            } else {


            }
        }
    }

    interface MainView {
        fun requestPermissions()
        fun checkSelfPermission(): Boolean
        fun showToast(message: String?)
    }
}