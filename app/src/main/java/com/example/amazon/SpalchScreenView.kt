package com.example.amazon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.amazon.LoginScreen.LoginPageScreen

class SpalchScreenView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalch_screen_view)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginPageScreen::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}