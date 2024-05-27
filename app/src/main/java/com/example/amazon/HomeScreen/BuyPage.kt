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
    private  var cartAdapter: CartAdapter ?= null
    private var userId: String? = null
    private var db: AppDataBase? = null
    private var productsInCart: List<CartItem>? = null
    private var subtotal: Double = 0.0


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
            val action = BuyPageDirections.actionBuyPageToPaymentPage(price = binding.BuyLastTotalPrice.text.toString())
            findNavController().navigate(action)
        }


    }

    private fun getProductsOfCurrentUserById() {
        if (userId != null && db != null) {
            productsInCart = db!!.cartDao().getCartItemsOfCurrentUserByUserId(userId!!)
        }
    }

    private fun showCart() {
        getProductsOfCurrentUserById()
        if (productsInCart?.size!! > 0) {
            cartAdapter =CartAdapter(productsInCart!!)
            cartProductRecyclerView.adapter =cartAdapter
            cartProductProgressBar.visibility = View.GONE
            cartProductRecyclerView.visibility = View.VISIBLE
            cartProductNoDataFound.visibility = View.GONE
            calculateTotalPrice()
            addClickListener()
            binding.TotalItems.text=resources.getString(R.string.itemsInCart,productsInCart!!.size)



        } else {
            cartProductProgressBar.visibility = View.GONE
            cartProductRecyclerView.visibility = View.GONE
            cartProductNoDataFound.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "The Your Cart is Empty ", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun addClickListener() {
        cartAdapter?.setOnItemClickListener(object : CartAdapter.ClickListener {
            override fun onPlusClick(productId: Int) {

            }

            override fun onMinusClick(productId: Int) {

            }

            override fun onRemoveClick(productId: Int) {
                removeProductFromCart(productId, userId!!)
                getProductsOfCurrentUserById()
                showCart()
            }

        })

    }

    private fun removeProductFromCart(productId: Int ,userId:String ) {
        if (db != null) {
            db!!.cartDao().deleteProductFromCart(userId, productId)
        }

    }
    private fun incrementProductQuantity(productId: Int  ) {
        if (db != null) {
            val currentProduct= db!!.cartDao().getProductById(productId)
            currentProduct.quantity++
            db!!.cartDao().updateCartItem(currentProduct)
        }

    }
    private fun decrementProductQuantity(productId: Int  ) {
        if (db != null) {
            val currentProduct = db!!.cartDao().getProductById(productId)
            if (currentProduct.quantity ==1)
            db!!.cartDao().updateCartItem(currentProduct)
            else {
                currentProduct.quantity--
                db!!.cartDao().updateCartItem(currentProduct)
            }

        }

    }
    private fun calculateTotalPrice() {
        var res= 0.0
        if (productsInCart != null) {
            for (product in productsInCart!!) {
                res += product.totalPrice
            }
            this.subtotal=res
            binding.BuyDeliveryPriceNumber.text=resources.getString(R.string.cart_price,100.toString())
            binding.BuySubTotalPriceNum.text=resources.getString(R.string.cart_price,subtotal.toString())
            binding.BuyLastTotalPrice.text=resources.getString(R.string.cart_price,(subtotal+100).toString())
        }else{
            Toast.makeText(requireContext(),"No Products In Cart",Toast.LENGTH_LONG).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}