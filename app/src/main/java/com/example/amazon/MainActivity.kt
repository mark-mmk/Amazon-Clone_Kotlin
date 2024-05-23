package com.example.amazon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.amazon.HomeScreen.MenuPage
import com.example.amazon.HomeScreen.ProfileFragment
import com.example.amazon.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var AppBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    lateinit var btnNav: BottomNavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
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

        drawerLayout=binding.drawerLayout
        showDrawer()
        binding.bottomnavigation.menu.getItem(3).setOnMenuItemClickListener {
            Toast.makeText(this@MainActivity, "Coming Soon", Toast.LENGTH_SHORT).show()
            drawerLayout.openDrawer(binding.navView)
            true}

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(AppBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun showDrawer(){

    supportFragmentManager.beginTransaction()
        .replace(R.id.MainDrawerFrameLayout, ProfileFragment())
        .commit()
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this@MainActivity, drawerLayout,
                R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
//            true
//        } else super.onOptionsItemSelected(item)
//    }

}
