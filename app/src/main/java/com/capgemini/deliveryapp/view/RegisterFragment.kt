package com.capgemini.deliveryapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.capgemini.deliveryapp.R
import com.capgemini.deliveryapp.presenter.RegisterFragmentPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        if (fAuth.currentUser != null) {
            Toast.makeText(activity, "User Already Logged In", Toast.LENGTH_LONG).show()
            Log.d("current user", fAuth.currentUser.toString())
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

    private fun onRegisterClick() {
        showProgressBar()
        val name = rnameE.text.toString().trim()
        val email = remailE.text.toString().trim()
        val pass = rpassE.text.toString().trim()
        val phone = rphoneE.text.toString().trim()
        //validateEmail
        val emailvalidated = presenter.validateEmail(email)
        //validate password
        val passwordvalidated = presenter.validatePassword(pass)
        //create user with email and password
        if (emailvalidated == true and passwordvalidated == true)
            presenter.createFirebaseUserWithEmailAndPassword(name, phone, email, pass)

    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        Log.d("progress bar ", "progress bar hidden")
    }

    override fun showToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


}