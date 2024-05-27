package com.example.amazon.HomeScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.amazon.Adapter.CategoryAdapter
import com.example.amazon.R
import com.example.amazon.RetrofitHelper
import com.example.amazon.dataBase.AppDataBase
import com.example.amazon.dataBase.CartItem

import com.example.amazon.databinding.FragmentHomePageBinding
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

class HomePage : Fragment() {
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categoriesProgressBar: ProgressBar
    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var productsProgressBar: ProgressBar
    private lateinit var productsNoDataFound: ImageView
    private lateinit var categories: ArrayList<String>
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductsAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private var userId: String? = null
    private var db: AppDataBase? = null
    private var productsIds: List<Int>? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productsProgressBar = view.findViewById(R.id.ProductsProgressBar)
        productsNoDataFound = view.findViewById(R.id.ProductsNoDataFoundImg)
        productsRecyclerView = view.findViewById(R.id.ProductsRecyclerView)
        firebaseAuth = FirebaseAuth.getInstance()
        userId = firebaseAuth.currentUser?.uid
        db = AppDataBase.DatabaseBuilder.getInstance(requireContext())
        productsRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)


        categoriesProgressBar = binding.HomeCategoryProgressBar
        categoriesRecyclerView = binding.HomeRecyclerCategory
        categoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        if (userId != null) {
            getProductsIdsOfCurrentUserById(userId)

        }
        getAllCategories()
        getLimitProducts()

        binding.HomeProductsSeeMoreTV.setOnClickListener {
            navigationToProducts("products")
        }


    }

    private fun navigationToProducts(categoryName: String) {
        val action = HomePageDirections.actionHomePageToAllProductsFragment(categoryName)
        findNavController().navigate(action)
    }


    private fun getLimitProducts(limit: Int = 8) {
        productsProgressBar.visibility = View.VISIBLE
        productsRecyclerView.visibility = View.GONE
        productsNoDataFound.visibility = View.GONE
        RetrofitHelper.getInstance()
            .getLimitProducts(limit).enqueue(object : Callback<ProductsResponseArr> {
                override fun onResponse(
                    call: Call<ProductsResponseArr>,
                    response: Response<ProductsResponseArr>
                ) {
                    if (response.isSuccessful && response.body()!!.size > 0) {

                        val products = response.body()!!
                        productAdapter = ProductsAdapter(products, requireContext(),productsIds)
                        addClickListener()
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

    private fun addClickListener() {
        productAdapter.setOnItemClickListener(object : ProductsAdapter.ProductClickListener {
            override fun onCartClick(product: ProductResponseItem) {
                onImageAddToCartClick(product)
            }

            override fun onImageClick(product: ProductResponseItem) {
                val action =
                    HomePageDirections.actionHomePageToProductsDescription(product.id.toString())
                findNavController().navigate(action)
                Toast.makeText(requireContext(),"Product Description",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getProductsIdsOfCurrentUserById(user_id: String?) {
        if (user_id!=null&&db!=null){
            productsIds=db!!.cartDao().getListOfProductsIdsOfCurrentUserById(user_id)
        }


    }


    //add click listener to image to add to cart
    private fun onImageAddToCartClick(product: ProductResponseItem) {

        if (userId != null) {
            if (isProductInCart(product.id)) {
                db!!.cartDao().deleteProductFromCart(userId!!, product.id)
                Toast.makeText(requireContext(), "Deleted From Cart ", Toast.LENGTH_SHORT).show()

            } else {

                    db!!.cartDao().insertProductToCart(
                        CartItem(
                            0, userId!!, product.id, 1, product.price,
                            (product.price * 1), product.image, product.title,
                        )
                    )
                    Toast.makeText(requireContext(), "Added To Cart", Toast.LENGTH_SHORT).show()
                }
            updateProductsIds(userId!!)
        } else {
            Toast.makeText(
                requireContext(), "Please Login First No User!!", Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun updateProductsIds(userId: String) {
        productsIds = db!!.cartDao().getListOfProductsIdsOfCurrentUserById(userId)
        productAdapter.productsIds = productsIds
    }

    private fun isProductInCart(productId: Int): Boolean {
        return productsIds?.contains(productId) == true
    }

    private fun getAllCategories() {
        categoriesProgressBar.visibility = View.VISIBLE
        categoriesRecyclerView.visibility = View.GONE

        RetrofitHelper.getInstance().getAllCategories()
            .enqueue(object : Callback<ArrayList<String>> {
                override fun onResponse(
                    call: Call<ArrayList<String>>,
                    response: Response<ArrayList<String>>
                ) {
                    if (response.isSuccessful && response.body()!!.size > 0) {
                        val categories = response.body()!!
                        categoryAdapter = CategoryAdapter(categories)
                        this@HomePage.categories = categories
                        categoriesRecyclerView.adapter = categoryAdapter
                        categoriesProgressBar.visibility = View.GONE
                        categoriesRecyclerView.visibility = View.VISIBLE
                        categoryClick()


                    } else {
                        Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
                    categoriesProgressBar.visibility = View.GONE
                    categoriesRecyclerView.visibility = View.GONE
                    Toast.makeText(requireContext(), " Error: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }

    private fun categoryClick() {
        categoryAdapter.setOnCategoryClickListener(
            object : CategoryAdapter.CategoryClickListener {
                override fun onCategoryClick(categoryName: String) {
                    navigationToProducts(categoryName)
                }

            })
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}