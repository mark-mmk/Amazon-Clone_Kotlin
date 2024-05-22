package com.example.amazon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.amazon.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var AppBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    lateinit var btnNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host_fragment_content_main)
        btnNav = findViewById(R.id.bottomnavigation)
        btnNav.setupWithNavController(navController)
        setSupportActionBar(binding.apparAll)
        val NavController = findNavController(R.id.nav_host_fragment_content_main)
        AppBarConfiguration = AppBarConfiguration(NavController.graph)
        setupActionBarWithNavController(NavController, AppBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(AppBarConfiguration) || super.onSupportNavigateUp()
    }

}
