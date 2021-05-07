package com.capgemini.deliveryapp.view.deliveryactivity.slidemenu.logout

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.capgemini.deliveryapp.R
import com.google.firebase.auth.FirebaseAuth
import com.capgemini.deliveryapp.Repository.DBWrapper
import com.capgemini.deliveryapp.presenter.LogoutFragmentPresenter
import com.capgemini.deliveryapp.view.mainactivity.MainActivity


class LogoutFragment : Fragment(), LogoutFragmentPresenter.LogoutView {
    val PREF_NAME = "Credentials"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

    //show an alert dialog warning user about logout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val presenter = LogoutFragmentPresenter(this)
        var builder = AlertDialog.Builder(activity)
        builder.setMessage(getString(R.string.logoutsure))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                // on yes selected,delete sharedpref and database
                presenter.signoutFireBase()
                val wrapper2 = DBWrapper(requireContext())
                wrapper2.deleteAll()
                //delete sharedpref
                presenter.deleteSharedPrefLocation()
                deleteSharedPrefLocation(requireContext())
                //navigate to main activity and pop out the current activity
                navigateToHomeScreen()

            }
            .setNegativeButton(getString(R.string.no)) { _, _ ->
                fragmentManager?.popBackStack()
            }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

        super.onViewCreated(view, savedInstanceState)
    }

    fun deleteSharedPrefLocation(context: Context) {
        val pref = context.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        pref.edit().remove("location").commit()

    }

    override fun gettheSharedPreferences(): SharedPreferences? {

        return (requireContext()).getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
    }

    override fun navigateToHomeScreen() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
