package com.capgemini.deliveryapp.presenter

import android.util.Log
import com.capgemini.firebasedemo.AppData.FireBaseWrapper
import com.google.firebase.auth.FirebaseAuth

class LoginFragmentPresenter(val view: LoginView) {
    val fAuth: FirebaseAuth = FirebaseAuth.getInstance()


    // if email is null or empty,return false.This is to prevent Null Pointer Exception
    fun isEmailValid(username: String): Boolean {


        return !(username.isNullOrEmpty())


    }

    // if password is null or empty,return false.This is to prevent Null Pointer Exception
    fun isPasswordValid(password: String): Boolean {
        //       view.showToast("password is invalid")
        return !(password.isNullOrEmpty())
    }

    // once the email and password are validated in view, sign in with those credentials. navigate()
    // callback is called on success to navigate to DeliveryActivity
    fun signInFirebaseWithEmailAndPassword(
        email: String,
        password: String,
        navigate: () -> (Unit)
    ) {
        view.showProgressBar()
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {

                Log.d("registermvp", "success")
                navigate()
            } else {
                view.showToast("Error! ${it.exception?.message}")
                Log.d("register", "fail :: ${it.exception?.message}")
            }
            view.hideProgressBar()
        }
    }

    // send a reset email after email is validated
    fun sendResetEmail(email: String) {
        val wrapper = FireBaseWrapper()
        wrapper.resetEmail(email) {
            Log.d("resetpass", "$it")
            view.ShowSnackBar(it)
        }

    }


    interface LoginView {

        fun showProgressBar()
        fun hideProgressBar()
        fun ShowSnackBar(snack: String)
        fun showToast(message: String?)
    }
}