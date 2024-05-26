package com.example.amazon.Adapter

import android.annotation.SuppressLint
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


class CartAdapter(private var list: List<CartItem>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var clickListener: ClickListener? = null

    class ViewHolder(binding: RowCartItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        val imageProductCart: ImageView = binding.RowCartProductImage
        val titleProductCart: TextView = binding.RowCartProductTitle
        val priceProductCart: TextView = binding.RowCartProductPrice
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
        holder.priceProductCart.text = cartProduct.price.toString()
        holder.quantityProductCart.text = cartProduct.quantity.toString()

        holder.plusProductCart.setOnClickListener {
            clickListener?.onPlusClick(position)
        }
        holder.minusProductCart.setOnClickListener {
            clickListener?.onMinusClick(position)
        }
        holder.removeProductCart.setOnClickListener {
            clickListener?.onRemoveClick(cartProduct.productId)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<CartItem>) {
        list = newList
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun onPlusClick(productId: Int)
        fun onMinusClick(productId: Int)
        fun onRemoveClick(productId: Int)
    }
}


