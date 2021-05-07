package com.capgemini.deliveryapp.presenter

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginFragmentPresenter(val view : LoginView ) {
    val fAuth : FirebaseAuth

    init {
        fAuth = FirebaseAuth.getInstance()
    }
    fun signInFirebaseWithEmailAndPassword(email : String,password : String)
    {   view.showProgressBar()
        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful)
            {
                view.showToast("Login SUccessful")
                //Toast.makeText(activity, "Login successful ", Toast.LENGTH_LONG).show()
                Log.d("registermvp","success")
                view.hideProgressBar()
            }
            else
            {   view.showToast("Error! ${it.exception?.message}")
              //  Toast.makeText(activity, "Error! ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                Log.d("register","fail :: ${it.exception?.message}")
                view.hideProgressBar()
            }
        }

    }







    interface LoginView {

        fun showProgressBar()
        fun hideProgressBar()

        fun showToast(message: String?)
    }
}