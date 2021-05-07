package com.capgemini.deliveryapp.view.mainactivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.capgemini.deliveryapp.R
import androidx.navigation.fragment.findNavController
import com.capgemini.deliveryapp.view.deliveryactivity.DeliveryActivity
import com.capgemini.firebasedemo.AppData.FireBaseWrapper
import com.google.firebase.auth.FirebaseAuth


class SplashScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler().postDelayed({
            CheckUserLoggedIn()

        }, 2500)
    }

    //check if user is logged in.If yes, navigate to DeliveryActivity
    fun CheckUserLoggedIn() {
        val fAuth = FirebaseAuth.getInstance()
        if (fAuth.currentUser != null) {

            val wrapper = FireBaseWrapper()
            wrapper.getUserFromId(fAuth.currentUser.uid) {
                Log.d("obtainedusernamelogin", it.name)
            }
            Log.d("sessiontokenlambda", fAuth.currentUser.uid.toString())
            Log.d("sessiontokenlambda", "tokensession")
            val x = fAuth.currentUser.getIdToken(true)
            Log.d("sessiontokenlambda", x.toString())
            //send intent to DeliverActivity
            activity?.finish()
            val intent = Intent(activity, DeliveryActivity::class.java)
            requireActivity().finish()
            startActivity(intent)

        } else {
            findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
        }


    }
}