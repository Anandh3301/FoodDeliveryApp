package com.capgemini.deliveryapp.view.checkoutactivity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.capgemini.deliveryapp.R
import com.capgemini.deliveryapp.Repository.DBWrapper
import com.capgemini.deliveryapp.presenter.CheckoutActivityPresenter
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_checkout.*
import java.util.*

class CheckoutActivity : AppCompatActivity(), CheckoutActivityPresenter.CheckoutView {
    val PREF_NAME = "Credentials"
    var total1 = 0

    //check internet connectivity .If no connection,show infinite alertDialog
    override fun onResume() {
        super.onResume()
        checkInternetConnectivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        val presenter = CheckoutActivityPresenter(this)

        //retrieve total
        val wrapper = DBWrapper(this)
        val total = wrapper.calculateBill()
        total1 = total
        showBill(" ${total}")

        //retrieve order details as string
        val order = wrapper.retrieveCartAsString()
        //save Order details as new document to Order Snapshot of current user
        val fAuth = FirebaseAuth.getInstance()
        presenter.saveORderDetails(fAuth.currentUser.uid, order, total)

        sendNotification()
        wrapper.updateAllQuantitytoZero()
    }

    //get address
    override fun getSharedPrefLocation(): String {
        val pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val user = pref.getString("location", "")
        return user!!

    }

    //show bill to user in thank you text
    override fun showBill(bill: String) {

        thankyoutxt.text = getString(R.string.thanktext) + "\n ₹${bill}"
    }
//आदेश देने के लिए धन्यवाद


    override fun showToast(message: String?) {
        //
    }

    private fun sendNotification() {
        //1. Get the reference of notification manager
        val nManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        //NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE) in java


        //2. Create Notification
        lateinit var builder: Notification.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "test", "AndroidUI",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            nManager.createNotificationChannel(channel)//register channel

            builder = Notification.Builder(this, "test")
        } else {
            builder = Notification.Builder(this)
        }

        //₹
        builder.setSmallIcon(R.drawable.icon2)
        builder.setContentTitle("Tangito")
        builder.setContentText("${getString(R.string.thanktext)} ₹ ${total1}")
        builder.setAutoCancel(true)

        //val tryIntent = Intent(this, MainActivity::class.java)
        //val resumeIntent = PendingIntent.getService(this, 0,tryIntent,0)
        //val resumeAction = Notification.Action.Builder(android.R.drawable.ic_dialog_info,"Try Again").build()
        //builder.addAction(resumeAction)
        //create remote view(like Spotify)
//        val l = layoutInflater.inflate(R.layout.activity_main_constraint,null)
//        //set remote view
//        builder.setCustomContentView()


        // builder.setContentIntent(pi)

        val myNotification = builder.build()
//        myNotification.flags = Notification.FLAG_AUTO_CANCEL or  Notification.FLAG_NO_CLEAR
        //To make it non clear able


        //3. Send notification
        nManager.notify(1, myNotification)
    }


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
            builder.setTitle("No network connection")
            //set message for alert dialog
            builder.setMessage("please check your connection")
            // builder.setIcon(android.R.drawable.)

            //performing positive action
            builder.setPositiveButton("OK") { dialogInterface, which ->
                checkInternetConnectivity()
            }
            //performing cancel action
            builder.setNeutralButton("Cancel") { dialogInterface, which ->
                checkInternetConnectivity()
            }
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
            return false
        }
        //   return activeNetworkInfo != null && activeNetworkInfo.isConnected()

    }
}