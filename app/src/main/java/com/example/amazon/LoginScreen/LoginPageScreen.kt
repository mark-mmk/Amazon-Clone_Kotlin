package com.example.amazon.LoginScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.amazon.MainActivity
import com.example.amazon.databinding.ActivityLoginPageScreenBinding
import com.google.firebase.auth.FirebaseAuth


class LoginPageScreen : AppCompatActivity() {
    lateinit var binding:ActivityLoginPageScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.signIn.setOnClickListener {
            val email = binding.emailLogin.text.toString()
            val pass = binding.passwordLogin.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Please Enter Success Email and Password", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText( this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
        binding.forgetPassword.setOnClickListener {
            val email = binding.emailLogin.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()

            }

            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Failed to send password reset email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.register.setOnClickListener {
            val intent = Intent(
                this@LoginPageScreen,
                RegisterPageScreen::class.java
            )
            startActivity(intent)
        }
    }
    override fun onPause() {
        super.onPause()
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putString("user", binding.emailLogin.text.toString())
        myEdit.putString("password", binding.passwordLogin.text.toString())
        myEdit.apply()
    }
    override fun onResume() {
        super.onResume()
        val shared = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val Name = shared.getString("user", "")
        val Password = shared.getString("password", "")
        binding.emailLogin.setText(Name)
        binding.passwordLogin.setText(Password)
    }
    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}