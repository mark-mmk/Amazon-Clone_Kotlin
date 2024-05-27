package com.example.amazon.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.amazon.R
import com.example.amazon.dataBase.CartItem
import com.example.amazon.databinding.RowCartItemLayoutBinding
import com.squareup.picasso.Picasso
import kotlin.math.ceil


class CartAdapter( var list: List<CartItem>,private val context: Context) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var clickListener: ClickListener? = null

    class ViewHolder(binding: RowCartItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        val imageProductCart: ImageView = binding.RowCartProductImage
        val titleProductCart: TextView = binding.RowCartProductTitle
        val priceProductCart: TextView = binding.RowCartProductPrice
        val totalPriceOfProductCart: TextView = binding.RowCartProductTotalPrice
        val quantityProductCart: TextView = binding.RowCartProductTVQuantity
        val plusProductCart: TextView = binding.RowCartProductBtnPlus
        val minusProductCart: TextView = binding.RowCartProductBtnMines
        val removeProductCart: Button = binding.RowCartProductBtnRemove

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowCartItemLayoutBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n", "MissingInflatedId")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartProduct = list[position]
        Picasso.get().load(cartProduct.image)
            .placeholder(R.drawable.img_loading)
            .into(holder.imageProductCart)

        holder.titleProductCart.text = cartProduct.title
        holder.priceProductCart.text = context.resources.getString(R.string.cart_price
            ,cartProduct.price.toString())
        holder.quantityProductCart.text = cartProduct.quantity.toString()
        holder.totalPriceOfProductCart.text=context.resources.getString(R.string.cart_total_price_product
        , ceil(cartProduct.totalPrice).toString())

        holder.plusProductCart.setOnClickListener {
            clickListener?.onPlusClick(cartProduct,position)
        }
        holder.minusProductCart.setOnClickListener {
            clickListener?.onMinusClick(cartProduct,position)
        }
        holder.removeProductCart.setOnClickListener {
            clickListener?.onRemoveClick(cartProduct.productId,position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun onPlusClick(cartProduct: CartItem,position: Int)
        fun onMinusClick(cartProduct: CartItem ,position: Int)
        fun onRemoveClick(productId: Int,position: Int)
    }
}


