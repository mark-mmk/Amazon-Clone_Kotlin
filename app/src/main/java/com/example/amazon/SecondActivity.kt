package com.example.amazon

import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amazon.HomeScreen.AllProductsFragment
import com.example.amazon.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.SecondLayoutToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val categoryName = intent.getStringExtra("category_name")
        val productId = intent.getIntExtra("product_id", -1)

        if (productId != -1 && productId != null) {
            val bundle = Bundle()
            bundle.putInt("product_id", productId)
            val productsDescription = ProductsDescription()
            productsDescription.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.SecondLayoutFrameLayout, productsDescription)
                .commit()

        } else if (!categoryName.isNullOrEmpty()) {


            val bundle = Bundle()
            bundle.putString("category_name", categoryName)
            val allProductsFragment = AllProductsFragment()
            allProductsFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.SecondLayoutFrameLayout, allProductsFragment)
                .commit()
        }
    }
    override fun onSupportNavigateUp(): Boolean {

        onBackPressedDispatcher.onBackPressed()
        return true
    }
}


