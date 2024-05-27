package com.example.amazon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            .getproductsById(details).enqueue(object : Callback<ProductsResponseArr> {
                override fun onResponse(
                    call: Call<ProductsResponseArr>,
                    response: Response<ProductsResponseArr>
                ) {
                    if (response.isSuccessful && response.body()!!.size > 0) {
                        productsArrayList.add(response.body()!![0])
                         Picasso.get()
                            .load(productsArrayList[0].image)
                            .placeholder(R.drawable.img_loading)
                            .into(binding.image)
                        binding.title.text = productsArrayList[0].title
                        binding.description.text = productsArrayList[0].description
                        binding.rBar.rating = productsArrayList[0].rating.rate.toFloat()
                        binding.price.text = productsArrayList[0].price.toString()
                    } else {
                        Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ProductsResponseArr>, t: Throwable) {
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