package com.example.amazon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.amazon.databinding.FragmentProductsDescriptionBinding
import com.example.amazon.products.ProductResponseItem
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsDescription : Fragment() {
    private var _binding: FragmentProductsDescriptionBinding? = null
    private val binding get() = _binding!!
    private val args: ProductsDescriptionArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productId = args.productId

        RetrofitHelper.getInstance()
            .getProductById(productId).enqueue(object : Callback<ProductResponseItem> {
                override fun onResponse(
                    call: Call<ProductResponseItem>,
                    response: Response<ProductResponseItem>
                ) {
                    if (response.isSuccessful) {
                        binding.image.isVisible = true
                        binding.liner.isVisible = true
                        binding.progress.isVisible = false
                        val product = response.body()!!
                        Picasso.get()
                            .load(product.image)
                            .placeholder(R.drawable.img_loading)
                            .into(binding.image)
                        binding.title.text = product.title
                        binding.description.text = product.description
                        binding.rBar.rating = product.rating.rate.toFloat()
                        binding.rBar.isEnabled=false
                        binding.price.text = product.price.toString()
                    } else {
                        Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ProductResponseItem>, t: Throwable) {
                    Toast.makeText(requireContext(), " Error: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                }

                })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsDescriptionBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

}