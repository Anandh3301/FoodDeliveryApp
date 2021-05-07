package com.capgemini.deliveryapp.presenter

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.capgemini.deliveryapp.Repository.DBWrapper
import com.capgemini.deliveryapp.view.deliveryactivity.DeliveryActivity
import com.capgemini.firebasedemo.AppData.FireBaseWrapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragmentPresenter(val view: View) {
    val fAuth: FirebaseAuth

    //run in init to make sure the reference exists whenever needed in class
    init {
        fAuth = FirebaseAuth.getInstance()
    }

    // make sure password is atleast 6 characters long. isNullOrEmpty prevents Null Pointer Exception
    fun validatePassword(password: String): Boolean {
        if (password.isNullOrEmpty() || password.length < 6) {
            view.showToast("password must be at-least 6 characters")

            return false

        } else
            return true
    }

    //validates email in the sdame way as password.Prevents NPE
    fun validateEmail(email: String): Boolean {
        if (email.isNullOrEmpty() || email.length < 6) {
            view.showToast("Enter a valid email")
            return false
        } else {
            return true
        }
    }

    // if user is logged in,directly navigate to DeliveryActivity using navigate() lambda
    fun CheckUserLoggedIn(navigate: () -> Unit) {
        val fAuth = FirebaseAuth.getInstance()
        if (fAuth.currentUser != null) {
            //Toast.makeText(activity,  "User Already Logged In", Toast.LENGTH_LONG).show()
            Log.d("current user", fAuth.currentUser.toString())
            val wrapper = FireBaseWrapper()
            wrapper.getUserFromId(fAuth.currentUser.uid) {
                Log.d("obtainedusernamereg", it.name)
            }
            navigate()
        }

    }

    //once email and password are validated,try and create new user with credentials(move to FirebaseWrapper())
    //if user creation is successful, save the data to a new user with StoreInfoOfCreatedUser()
    fun createFirebaseUserWithEmailAndPassword(
        name: String,
        phone: String,
        email: String,
        password: String,
        navigate: () -> (Unit)
    ) {
        view.showProgressBar()
        fAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    //store new user data into firebase
                    Log.d("register", "success")
                    StoreInfoOfCreatedUser(name, phone, email, password)
                    navigate()

                } else {
                    view.showToast("${it.exception?.message}")
                    view.hideProgressBar()
                    Log.d(
                        "register",
                        "fail :${it.exception?.message}"
                    )
                }
            }

    }

    //if new user creation is successful, store the name,email and phone no. to the firestore
    fun StoreInfoOfCreatedUser(name: String, phone: String, email: String, password: String) {
        // progress
        val UID = fAuth.currentUser.uid
        val fstore = FirebaseFirestore.getInstance()
        val documentReference = fstore
            .collection("users")
            .document(UID)

        val currentUser = HashMap<String, Any>()
        currentUser["name"] = name
        currentUser["phone"] = phone
        currentUser["email"] = email
        //  currentUser["password"] = password (getting rid of password)
        // no longer storing password as it is a security risk
        documentReference.set(currentUser).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("user", "created : $currentUser")


            }


        }


    }


    interface View {

        fun showProgressBar()
        fun hideProgressBar()
        fun navigateToDelivery()
        fun showToast(message: String?)
    }
}