package com.capgemini.deliveryapp.presenter

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CheckoutActivityPresenter(val view: CheckoutView) {
    //save this document to orders collection of current user
    fun saveORderDetails(UID: String, order: String, totla: Int) {
        val docData = hashMapOf(
            "order" to order,
            "location" to view.getSharedPrefLocation(),
            "total" to totla,
            "date" to Timestamp(Date()),

            )

        //move to FireBaseWrapper()
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(UID).collection("orders")
            .add(docData)
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
    }


    interface CheckoutView {
        fun showBill(bill: String)

        fun getSharedPrefLocation(): String
        fun showToast(message: String?)
    }
}