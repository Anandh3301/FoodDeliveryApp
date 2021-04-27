package com.capgemini.deliveryapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.capgemini.deliveryapp.R
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {
    lateinit var emailE : EditText
    lateinit var passE: EditText
    lateinit var loginB : Button
    lateinit var regT: TextView

    val fAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        emailE = view.findViewById<EditText>(R.id.lnameE)
        passE = view.findViewById<EditText>(R.id.lpassE)
        loginB = view.findViewById<Button>(R.id.loginB)
        regT = view.findViewById<TextView>(R.id.regT)

        loginB.setOnClickListener {
            onLoginClicked()
        }

        regT.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun onLoginClicked() {
        val email= emailE.text.toString().trim()
        val pass= passE.text.toString().trim()
        fAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
            if(it.isSuccessful)
            {
                Toast.makeText(activity, "Login successful ", Toast.LENGTH_LONG).show()
                Log.d("register","success")
            }
            else
            {
                Toast.makeText(activity, "Error! ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                Log.d("register","fail :: ${it.exception?.message}")
            }
        }
    }

}