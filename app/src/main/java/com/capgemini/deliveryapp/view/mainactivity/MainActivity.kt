package com.capgemini.deliveryapp.view.mainactivity

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capgemini.deliveryapp.R
import com.capgemini.deliveryapp.presenter.MainActivityPresenter
import com.capgemini.firebasedemo.AppData.FireBaseWrapper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async


class MainActivity : AppCompatActivity(), MainActivityPresenter.MainView {
    val presenter = MainActivityPresenter(this)
    override fun onStart() {
        super.onStart()
        //checking permissions
        presenter.checkPermission()
    }

    override fun onResume() {
        super.onResume()
        checkInternetConnectivity()
    }
// may 5 9 44pm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                //      initializeFragments()
            } else {
                openAlertDialog(AlertDialog.Builder(this))
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //keeping in view as alertdialog is a ui element?
    private fun openAlertDialog(alertDialogBuilder: AlertDialog.Builder) {

        alertDialogBuilder.setMessage(getString(R.string.alertm))
        alertDialogBuilder.setPositiveButton(
            getString(R.string.tryagain),
            DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                presenter.checkPermission()
            })
        alertDialogBuilder.setNegativeButton(
            getString(R.string.settings),
            DialogInterface.OnClickListener { dialog, which ->
                val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                i.addCategory(Intent.CATEGORY_DEFAULT)
                i.data = Uri.parse("package:com.capgemini.deliveryapp")
                startActivity(i)
            })
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun showToast(message: String?) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    //requesting permission for location
    override fun requestPermissions() {
        requestPermissions(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), 1
        )
    }

    override fun checkSelfPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
    }


    //checking internet connectivity
    fun checkInternetConnectivity(): Boolean {

        val connectivityManager: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.getActiveNetworkInfo()
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            return true
//do nothing
        } else {
            Log.d("internet", "internet")
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle(getString(R.string.nonetwork))
            //set message for alert dialog
            builder.setMessage(getString(R.string.connectioncheck))
            // builder.setIcon(android.R.drawable.)

            //performing positive action
            builder.setPositiveButton(getString(R.string.yes)) { dialogInterface, which ->
                checkInternetConnectivity()
            }
            //performing cancel action
            builder.setNeutralButton(getString(R.string.no)) { dialogInterface, which ->
                checkInternetConnectivity()
            }
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
            return false
        }


    }

}