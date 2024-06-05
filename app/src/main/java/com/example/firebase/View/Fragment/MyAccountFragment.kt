package com.example.firebase.View.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.firebase.R
import com.example.firebase.View.Activity.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyAccountFragment : Fragment() {

    private lateinit var signOutButton: Button
    private lateinit var usernameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_account, container, false)

        signOutButton = view.findViewById(R.id.signOutButton)
        usernameTextView = view.findViewById(R.id.usernameTextView)

        signOutButton.setOnClickListener {
            signOut()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserInfo()
    }

    private fun loadUserInfo() {
        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {
            // Set username
            val username = currentUser.email
            usernameTextView.text = "Username: $username"
        } else {

        }
    }

    private fun signOut() {
        val auth: FirebaseAuth = Firebase.auth
        auth.signOut()
        val intent = Intent(activity, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}


