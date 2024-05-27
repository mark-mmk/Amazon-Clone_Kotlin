package com.example.amazon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.amazon.HomeScreen.AllProductsFragmentArgs
import com.example.amazon.databinding.FragmentAllProductsBinding
import com.example.amazon.databinding.FragmentHomePageBinding
import com.example.amazon.databinding.FragmentProductsDescriptionBinding
import com.example.amazon.products.ProductResponseItem
import com.example.amazon.products.ProductsAdapter
import com.example.amazon.products.ProductsResponseArr
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsDescription : Fragment() {
    private var _binding: FragmentProductsDescriptionBinding? = null
    private val binding get() = _binding!!
    val args : ProductsDescriptionArgs by navArgs()
    private val productsArrayList: ArrayList<ProductResponseItem> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val details =args.productId

        RetrofitHelper.getInstance()
            .getOneProductById(details.toInt()).enqueue(object : Callback<ProductResponseItem> {
                override fun onResponse(
                    call: Call<ProductResponseItem>,
                    response: Response<ProductResponseItem>
                ) {
                    if (response.isSuccessful) {
                        binding.image.isVisible=true
                        binding.liner.isVisible=true
                        binding.progress.isVisible=false
                        productsArrayList.add(response.body()!!)
                        Picasso.get()
                            .load(productsArrayList[0].image)
                            .placeholder(R.drawable.img_loading)
                            .into(binding.image)
                        binding.title.text = productsArrayList[0].title
                        binding.description.text = productsArrayList[0].description
                        binding.rBar.rating = productsArrayList[0].rating.rate.toFloat()
                        binding.rBar.isEnabled=false
                        binding.price.text = productsArrayList[0].price.toString()
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
        // Inflate the layout for this fragment
        _binding = FragmentProductsDescriptionBinding.inflate(inflater, container, false)
        return _binding!!.root    }

}