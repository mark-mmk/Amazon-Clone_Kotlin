package com.example.amazon.LoginScreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.INTERNET),
                1
            )
        } else {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo

            if (networkInfo != null && networkInfo.isConnected) {
                // Internet connection is available
                Toast.makeText(this@LoginPageScreen, "Network is Connected", Toast.LENGTH_LONG).show()

            } else {
                // Internet connection is not available
                Toast.makeText(this@LoginPageScreen, "Network is Not Connected", Toast.LENGTH_LONG)
                    .show()
            }
        }
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
        myEdit.apply()
    }
    override fun onResume() {
        super.onResume()
        val shared = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val Name = shared.getString("user", "")
        binding.emailLogin.setText(Name)
    }
    override fun onStart() {
        super.onStart()
        val email = binding.emailLogin.text.toString()
        if (firebaseAuth.currentUser != null && email != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}