package com.capgemini.deliveryapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.capgemini.deliveryapp.R
import com.capgemini.deliveryapp.Repository.DBWrapper
import com.capgemini.deliveryapp.presenter.LoginFragmentPresenter
import com.capgemini.firebasedemo.AppData.FireBaseWrapper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(), LoginFragmentPresenter.LoginView {


    lateinit var presenter: LoginFragmentPresenter
    val fAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = LoginFragmentPresenter(this)
        hideProgressBar()
        //get current user from fAuth. If not null, navigate
        presenter.CheckUserLoggedIn {
            val intent = Intent(
                activity,
                DeliveryActivity::class.java
            )
            startActivity(intent)

        }


        lLoginB.setOnClickListener {
            onLoginClicked()
        }

        lregT.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)

        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun onLoginClicked() {
        val email = lnameE.text.toString().trim()
        val pass = lpassE.text.toString().trim()
        presenter.signInFirebaseWithEmailAndPassword(email, pass) {
            //passing this as lambda to function, will call on success to navigate
            // to location pick
            val wrapper= context?.let {


                    it1 -> DBWrapper(it1) }
            Log.d("database3","database")
            //queries firebase for menu items,adds to internal db with quantity of 0
            wrapper?.addRowsFromFirebase()

            val intent = Intent(activity, DeliveryActivity::class.java)
            startActivity(intent)
        }

    }

    override fun showProgressBar() {
        Lprogressbar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        Lprogressbar.visibility = View.INVISIBLE
    }

    override fun showToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}