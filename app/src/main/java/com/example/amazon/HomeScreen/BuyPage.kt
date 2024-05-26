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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amazon.Adapter.CartAdapter
import com.example.amazon.R
import com.example.amazon.dataBase.AppDataBase
import com.example.amazon.dataBase.CartItem
import com.example.amazon.databinding.FragmentBuyPageBinding
import com.google.firebase.auth.FirebaseAuth

class BuyPage : Fragment() {
    private var _binding: FragmentBuyPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartProductRecyclerView: RecyclerView
    private lateinit var cartProductProgressBar: ProgressBar
    private lateinit var cartProductNoDataFound: ImageView
    private var userId: String? = null
    private var db: AppDataBase? = null
    private var productsInCart: List<CartItem>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBuyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = FirebaseAuth.getInstance().currentUser?.uid
        db = AppDataBase.DatabaseBuilder.getInstance(requireContext())

        cartProductProgressBar = view.findViewById(R.id.ProductsProgressBar)
        cartProductNoDataFound = view.findViewById(R.id.ProductsNoDataFoundImg)
        cartProductRecyclerView = view.findViewById(R.id.ProductsRecyclerView)
        cartProductRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)



        if (userId != null) {
            showCart()
        } else {
            Toast.makeText(requireContext(), "No Current User ", Toast.LENGTH_LONG).show()
        }


        binding.checkout.setOnClickListener {
            findNavController().navigate(R.id.action_buyPage_to_paymentPage)
        }
    }

    private fun getProductsOfCurrentUserById() {
        if (userId != null && db != null) {
           productsInCart= db!!.cartDao().getCartItemsOfCurrentUserByUserId(userId!!)
        }
    }

    private fun showCart() {
        getProductsOfCurrentUserById()
        if (productsInCart?.isNotEmpty() == true) {
            cartProductRecyclerView.adapter = CartAdapter(productsInCart!!)
            cartProductProgressBar.visibility = View.GONE
            cartProductRecyclerView.visibility = View.VISIBLE
            cartProductNoDataFound.visibility = View.GONE


        } else {
            cartProductProgressBar.visibility = View.GONE
            cartProductRecyclerView.visibility = View.GONE
            cartProductNoDataFound.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "The Your Cart is Empty ", Toast.LENGTH_LONG)
                .show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}