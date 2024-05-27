package com.example.amazon.HomeScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amazon.R
import com.example.amazon.RetrofitHelper
import com.example.amazon.dataBase.AppDataBase
import com.example.amazon.dataBase.CartItem
import com.example.amazon.databinding.FragmentAllProductsBinding
import com.example.amazon.products.ProductResponseItem
import com.example.amazon.products.ProductsAdapter
import com.example.amazon.products.ProductsResponseArr
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AllProductsFragment : Fragment() {
    private var _binding: FragmentAllProductsBinding? = null
    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var productsProgressBar: ProgressBar
    private lateinit var productsNoDataFound: ImageView
    private var userId: String? = null
    private var db: AppDataBase? = null
    private var productsIds: List<Int>? = null
    private val args: AllProductsFragmentArgs by navArgs()
    private var productAdapter: ProductsAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllProductsBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = FirebaseAuth.getInstance().currentUser?.uid
        db = AppDataBase.DatabaseBuilder.getInstance(requireContext())
        productsProgressBar = view.findViewById(R.id.ProductsProgressBar)
        productsNoDataFound = view.findViewById(R.id.ProductsNoDataFoundImg)
        productsRecyclerView = view.findViewById(R.id.ProductsRecyclerView)
        productsRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)

        val categoryName = args.categoryName
        if (categoryName == "products") {
            getAllProducts()

        } else {
            getAllProductsByCategoryName(categoryName)
        }
        //chang title of tool bar
        (requireActivity() as AppCompatActivity).supportActionBar?.title = categoryName.uppercase()
    }


    private fun getAllProducts() {
        productsProgressBar.visibility = View.VISIBLE
        productsRecyclerView.visibility = View.GONE
        productsNoDataFound.visibility = View.GONE
        RetrofitHelper.getInstance()
            .getAllProducts().enqueue(object : Callback<ProductsResponseArr> {
                override fun onResponse(
                    call: Call<ProductsResponseArr>,
                    response: Response<ProductsResponseArr>
                ) {
                    if (response.isSuccessful && response.body()!!.size > 0) {

                        val products = response.body()!!
                        val productAdapter = ProductsAdapter(products, requireContext())
                        productsRecyclerView.adapter = productAdapter
                        productsProgressBar.visibility = View.GONE
                        productsRecyclerView.visibility = View.VISIBLE
                        productsNoDataFound.visibility = View.GONE
                    } else {
                        Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ProductsResponseArr>, t: Throwable) {

                    productsProgressBar.visibility = View.GONE
                    productsRecyclerView.visibility = View.GONE
                    productsNoDataFound.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), " Error: ${t.message}", Toast.LENGTH_LONG)
                        .show()

                }

            })
    }

    private fun getAllProductsByCategoryName(categoryName: String) {
        productsProgressBar.visibility = View.VISIBLE
        productsRecyclerView.visibility = View.GONE
        productsNoDataFound.visibility = View.GONE
        RetrofitHelper.getInstance()
            .getSingleCategory(categoryName)
            .enqueue(object : Callback<ProductsResponseArr> {
                override fun onResponse(
                    call: Call<ProductsResponseArr>,
                    response: Response<ProductsResponseArr>
                ) {
                    if (response.isSuccessful && response.body()!!.size > 0) {

                        val products = response.body()!!
                        productAdapter = ProductsAdapter(products, requireContext())
                        productsRecyclerView.adapter = productAdapter
                        productsProgressBar.visibility = View.GONE
                        productsRecyclerView.visibility = View.VISIBLE
                        productsNoDataFound.visibility = View.GONE
                        addClickListener()
                    } else {
                        Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ProductsResponseArr>, t: Throwable) {

                    productsProgressBar.visibility = View.GONE
                    productsRecyclerView.visibility = View.GONE
                    productsNoDataFound.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), " Error: ${t.message}", Toast.LENGTH_LONG)
                        .show()

                }

            })
    }

    private fun addClickListener() {
        productAdapter?.setOnItemClickListener(object : ProductsAdapter.ProductClickListener {
            override fun onCartClick(product: ProductResponseItem) {
                onImageAddToCartClick(product)
            }

            override fun onImageClick(product: ProductResponseItem) {
                val action =
                    HomePageDirections.actionHomePageToProductsDescription(product.id.toString())
                findNavController().navigate(action)
            }
        })
    }

    private fun onImageAddToCartClick(product: ProductResponseItem) {

        if (userId != null) {
            if (isProductInCart(product.id)) {
                db!!.cartDao().deleteProductFromCart(userId!!, product.id)
                Toast.makeText(requireContext(), "Deleted From Cart ", Toast.LENGTH_LONG).show()
            } else {

                db!!.cartDao().insertProductToCart(
                    CartItem(
                        0, userId!!, product.id, 1, product.price,
                        (product.price * 1), product.image, product.title,
                    )
                )
                Toast.makeText(requireContext(), "Added To Cart", Toast.LENGTH_LONG).show()
            }
            updateProductsIds(userId!!)
        } else {
            Toast.makeText(
                requireContext(), "Please Login First No User!!", Toast.LENGTH_LONG
            ).show()
        }

    }


    private fun isProductInCart(productId: Int): Boolean {
        return productsIds?.contains(productId) == true
    }

    private fun updateProductsIds(userId: String) {
        productsIds = db!!.cartDao().getListOfProductsIdsOfCurrentUserById(userId)
        productAdapter?.productsIds = productsIds
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}