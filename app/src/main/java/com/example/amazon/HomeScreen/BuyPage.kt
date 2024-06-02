package com.example.amazon.HomeScreen


import android.content.Intent
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
import com.example.amazon.SecondActivity
import com.example.amazon.dataBase.AppDataBase
import com.example.amazon.dataBase.CartItem
import com.example.amazon.databinding.FragmentBuyPageBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.ceil

class BuyPage : Fragment() {
    private var _binding: FragmentBuyPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartProductRecyclerView: RecyclerView
    private lateinit var cartProductProgressBar: ProgressBar
    private lateinit var cartProductNoDataFound: ImageView
    private var cartAdapter: CartAdapter? = null
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
//            val action = BuyPageDirections.actionBuyPageToPaymentPage(price = binding.BuyLastTotalPrice.text.toString())
//            findNavController().navigate(action)
            val i = Intent(requireContext(), SecondActivity::class.java)
            i.putExtra("lastTotalPrice",binding.BuyLastTotalPrice.text.toString())
            startActivity(i)
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
            cartAdapter = CartAdapter(productsInCart!!, requireContext())
            cartProductRecyclerView.adapter = cartAdapter
            cartProductProgressBar.visibility = View.GONE
            cartProductRecyclerView.visibility = View.VISIBLE
            cartProductNoDataFound.visibility = View.GONE
            calculateTotalPrice()
            addClickListener()
            binding.TotalItems.text =
                resources.getString(R.string.itemsInCart, productsInCart!!.size)


        } else {
            binding.TotalItems.text = resources.getString(R.string.itemsInCart, 0)
            cartProductProgressBar.visibility = View.GONE
            cartProductRecyclerView.visibility = View.GONE
            cartProductNoDataFound.visibility = View.VISIBLE
//            Toast.makeText(requireContext(), "The Your Cart is Empty ", Toast.LENGTH_LONG)
//                .show()
        }
    }

    private fun addClickListener() {
        cartAdapter?.setOnItemClickListener(object : CartAdapter.ClickListener {

            override fun onPlusClick(cartProduct: CartItem, position: Int) {
                incrementProductQuantity(cartProduct)
                updateLayout(position)
            }

            override fun onMinusClick(cartProduct: CartItem, position: Int) {
                if (cartProduct.quantity == 1)
                    Toast.makeText(requireContext(), "Product Quantity is 1", Toast.LENGTH_SHORT)
                        .show()
                else {
                    decrementProductQuantity(cartProduct)
                    updateLayout(position)
                }
            }


            override fun onRemoveClick(productId: Int, position: Int) {

                removeProductFromCart(productId, userId!!)
                getProductsOfCurrentUserById()
                cartAdapter?.list = productsInCart!!
                cartAdapter?.notifyItemRemoved(position)
                cartAdapter?.notifyItemRangeChanged(position, productsInCart!!.size)
                cartAdapter?.notifyDataSetChanged()
                calculateTotalPrice()
                if (productsInCart!!.size == 0) {
                        binding.TotalItems.text = resources.getString(R.string.itemsInCart, 0)
                        binding.BuyDeliveryPriceNumber.text =
                            resources.getString(R.string.checkout_price, 0.toString())
                    binding.BuySubTotalPriceNum.text=resources.getString(R.string.checkout_price,0.toString())
                    binding.BuyLastTotalPrice.text=resources.getString(R.string.checkout_price,0.toString())

                }


            }
        })
    }

    private fun updateLayout(position: Int) {
        getProductsOfCurrentUserById()
        cartAdapter?.list = productsInCart!!
        cartAdapter?.notifyItemChanged(position)
        calculateTotalPrice()
    }

    private fun removeProductFromCart(productId: Int, userId: String) {
        if (db != null) {
            db!!.cartDao().deleteProductFromCart(userId, productId)
        }
    }

    private fun incrementProductQuantity(product: CartItem) {
        if (db != null) {
            product.quantity++
            product.totalPrice = product.price * product.quantity
            db!!.cartDao().updateCartItem(product)
        }
    }

    private fun decrementProductQuantity(product: CartItem) {
        if (db != null) {
            product.quantity--
            product.totalPrice = product.price * product.quantity
            db!!.cartDao().updateCartItem(product)
        }
    }

    private fun calculateTotalPrice() {
        var res = 0.0
        if (productsInCart != null) {
            for (product in productsInCart!!) {
                res += product.totalPrice
            }
            this.subtotal = res
            binding.BuyDeliveryPriceNumber.text =
                resources.getString(R.string.checkout_price, 100.toString())
            binding.BuySubTotalPriceNum.text = resources.getString(
                R.string.checkout_price,
                ceil(subtotal).toString()
            )
            binding.BuyLastTotalPrice.text =
                resources.getString(R.string.checkout_price, ceil(subtotal + 100).toString())
        } else {
            Toast.makeText(requireContext(), "No Products In Cart", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}