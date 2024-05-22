package com.example.amazon.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.amazon.R
import com.example.amazon.test.carttest


class CartAdapter(private var list: ArrayList<carttest>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val card_product: ImageView = itemView.findViewById(R.id.cart_product)
        val card_product_name: TextView = itemView.findViewById(R.id.cart_product_name)
        val card_product_price: TextView = itemView.findViewById(R.id.cart_product_price)
        val card_product_size: TextView = itemView.findViewById(R.id.cart_product_Size)
        val add: AppCompatButton = itemView.findViewById(R.id.button_cart_item_add)
        val mines: AppCompatButton = itemView.findViewById(R.id.button_cart_item_mines)
        val Count: TextView = itemView.findViewById(R.id.cart_item_count)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        )
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n", "MissingInflatedId")
    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        holder.card_product.setImageResource(R.drawable.icon)
        holder.card_product_name.setText("mark mekhail")
        holder.card_product_price.setText("100")
        holder.card_product_size.setText("8")
        var count: Int = 0
        holder.add.setOnClickListener {
            count++
            holder.Count.text = count.toString()

        }

        holder.mines.setOnClickListener {
            if(count > 0){
                count--
                holder.Count.text = count.toString()
            }else{
                holder.Count.text = count.toString()
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}


