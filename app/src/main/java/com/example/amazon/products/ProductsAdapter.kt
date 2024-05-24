package com.example.amazon.products

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.amazon.R
import com.example.amazon.databinding.RowProductLayoutBinding
import com.squareup.picasso.Picasso

class ProductsAdapter(
    private val productsArrayList: ArrayList<ProductResponseItem>,
    private val context: Context
) :
    RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {
    class ProductsViewHolder(binding: RowProductLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var productImage = binding.RowProductImage
        var productTitle = binding.RowProductTitle
        var productPrice = binding.RowProductPrice
        var productRating = binding.RowProductRatingBar


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder(
            RowProductLayoutBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return productsArrayList.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = productsArrayList[position]
        holder.productTitle.text = product.title
        holder.productPrice.text = context.getString(R.string.price,product.price.toString())
        holder.productRating.rating = product.rating.rate.toFloat()

        Picasso.get()
            .load(product.image)
            .placeholder(R.drawable.img_loading)
            .into(holder.productImage)


    }
}