package com.example.amazon.LoginScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.amazon.R
import com.example.amazon.databinding.ActivityRegisterPageScreenBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterPageScreen : AppCompatActivity() {
    lateinit var binding: ActivityRegisterPageScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPageScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.cancel.setOnClickListener {
            val intent = Intent(this, LoginPageScreen::class.java)
            startActivity(intent)
        }
        binding.signUp.setOnClickListener {
            val email = binding.emailRegister.text.toString()
            val pass = binding.passwordRegister.text.toString()
            val confirmPass = binding.confirmPasswordRegister.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if(pass.length < 8 || confirmPass.length < 8 ){
                    Toast.makeText(this, "Please Enter a Password of more than 8 characters", Toast.LENGTH_SHORT).show()
                }
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, LoginPageScreen::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            }  else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }
}