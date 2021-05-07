package com.capgemini.deliveryapp.view.mainactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.capgemini.deliveryapp.R
import com.capgemini.deliveryapp.Repository.DBWrapper
import com.capgemini.deliveryapp.presenter.RegisterFragmentPresenter
import com.capgemini.deliveryapp.view.deliveryactivity.DeliveryActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*

// TODO: Rename parameter arguments, choose names that match

class RegisterFragment : Fragment(), RegisterFragmentPresenter.View {

    lateinit var progressBar: ProgressBar
    val fAuth = FirebaseAuth.getInstance()
    lateinit var presenter: RegisterFragmentPresenter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // if current user already logged in, navigate directly to dashboard
        presenter = RegisterFragmentPresenter(this)
        presenter.CheckUserLoggedIn {
            activity?.finish()
            val intent = Intent(activity, DeliveryActivity::class.java)
            requireActivity().finish()
            startActivity(intent)
        }


        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //replace with synthetic
        progressBar = view.findViewById(R.id.progressBar)
        hideProgressBar()
        //creating presenter to do business logic
        presenter = RegisterFragmentPresenter(this)
        rsignB.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }


        registerB.setOnClickListener {
            onRegisterClick()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    //registers a new user and moves to next activity
    private fun onRegisterClick() {

        val name = rnameE.text.toString().trim()
        val email = remailE.text.toString().trim()
        val pass = rpassE.text.toString().trim()
        val phone = rphoneE.text.toString().trim()
        //validateEmail
        val emailvalidated = presenter.validateEmail(email)
        Log.d("validated", emailvalidated.toString())
        val passwordvalidated = presenter.validatePassword(pass)
        Log.d("password validated", passwordvalidated.toString())
        //create user with email and password
        if ((emailvalidated && passwordvalidated)) {
            presenter.createFirebaseUserWithEmailAndPassword(name, phone, email, pass) {

                val wrapper = DBWrapper(requireContext())
                Log.d("database3reg", "database")

                wrapper?.addRowsFromFirebase()

                navigateToDelivery()
            }
        }

    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        Log.d("progress bar ", "progress bar hidden")
    }

    override fun navigateToDelivery() {
        val intent = Intent(
            activity,
            DeliveryActivity::class.java
        )
        requireActivity().finish()
        startActivity(intent)
    }

    override fun showToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


}