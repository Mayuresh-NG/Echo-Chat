package com.example.firebase.View.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class SignUpActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        signUpButton = findViewById(R.id.signUpButton)

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (validateInputs(email, password, confirmPassword)) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign up successful
                            val user = FirebaseAuth.getInstance().currentUser
                            user?.let {
                                addUserToFirestore(it)
                            }

                            Toast.makeText(baseContext, "Sign up successful.", Toast.LENGTH_SHORT).show()
                            // Navigate to main activity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(baseContext, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun validateInputs(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty()) {
            emailEditText.error = "Email is required"
            emailEditText.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Please enter a valid email"
            emailEditText.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            passwordEditText.error = "Password is required"
            passwordEditText.requestFocus()
            return false
        }

        if (password.length < 6) {
            passwordEditText.error = "Password should be at least 6 characters"
            passwordEditText.requestFocus()
            return false
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.error = "Please confirm your password"
            confirmPasswordEditText.requestFocus()
            return false
        }

        if (password != confirmPassword) {
            confirmPasswordEditText.error = "Passwords do not match"
            confirmPasswordEditText.requestFocus()
            return false
        }

        return true
    }

    private fun addUserToFirestore(user: FirebaseUser) {
        val db = FirebaseFirestore.getInstance()
        val userMap = hashMapOf(
            "email" to user.email
        )

        db.collection("users").document(user.uid)
            .set(userMap, SetOptions.merge())
            .addOnSuccessListener {
                // User successfully added to Firestore
            }
            .addOnFailureListener { exception ->
                Toast.makeText(baseContext, "Failed to add user to Firestore: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
