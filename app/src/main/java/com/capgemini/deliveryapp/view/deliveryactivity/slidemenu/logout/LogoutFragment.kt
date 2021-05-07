package com.capgemini.deliveryapp.view.deliveryactivity.ui.logout

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capgemini.deliveryapp.R
import com.google.firebase.auth.FirebaseAuth
import com.capgemini.deliveryapp.Repository.DBWrapper
import com.capgemini.deliveryapp.view.mainactivity.MainActivity


class LogoutFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var builder = AlertDialog.Builder(activity)
        builder.setMessage("Are you sure you want to Logout?")
            .setPositiveButton("YES"){_,_->
                val fAuth = FirebaseAuth.getInstance()
                fAuth.signOut()
                val wrapper= context?.let {


                        it1 -> DBWrapper(it1) }
               wrapper?.deleteAll()

                val intent = Intent(activity,
                    MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
            .setNegativeButton("NO"){_,_->
                fragmentManager?.popBackStack()
            }
        val alertDialog:AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

        super.onViewCreated(view, savedInstanceState)
    }
}