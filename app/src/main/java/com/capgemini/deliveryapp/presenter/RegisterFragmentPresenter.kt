package com.capgemini.deliveryapp.presenter

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragmentPresenter(val view: View) {
      val fAuth : FirebaseAuth
       init {
                fAuth = FirebaseAuth.getInstance()
       }
        fun validatePassword(password: String): Boolean {
                if (password.length < 6) {
                    view.showToast("password must be at-least 6 characters")

                        return false

                }
                else
                        return true
        }
                fun validateEmail(email: String) : Boolean{
                        if (email.isEmpty()) {
                            view.showToast("Enter a valid email")
                            return false
                        }
                        else
                        {
                                return true
                        }
                }

                fun createFirebaseUserWithEmailAndPassword(name: String, phone: String,email: String, password: String) {
                      view.showProgressBar()
                        fAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                                //store new user data into firebase
                                                Log.d("register", "success")
                                                StoreInfoOfCreatedUser(name,phone,email,password)

                                        } else {
                                              view.showToast("${it.exception?.message}")

                                                Log.d("register",
                                                        "fail :${it.exception?.message}")
                                        }
                                }

                }

                fun StoreInfoOfCreatedUser(name: String, phone: String,email: String, password: String) {
                   // progress
                        val UID = fAuth.currentUser.uid
                       val  fstore = FirebaseFirestore.getInstance()
                        val documentReference = fstore
                                .collection("users")
                                .document(UID)

                        val currentUser = HashMap<String, Any>()
                        currentUser["name"] = name
                        currentUser["phone"] = phone
                        currentUser["email"] = email
                        currentUser["password"] = password

                        documentReference.set(currentUser).addOnCompleteListener {
                                if (it.isSuccessful) {
                                        Log.d("user", "created : $currentUser")

                                        view.showToast("user created")
                                }
                          //  progress
                            view.hideProgressBar()
                        }


                }

    interface View {

        fun showProgressBar()
        fun hideProgressBar()

        fun showToast(message: String?)
    }
}