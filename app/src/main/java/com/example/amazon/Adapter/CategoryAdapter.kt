package com.example.amazon.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.amazon.R

class CategoryAdapter(private var list: ArrayList<String>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categoryClickListener: CategoryClickListener?=null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text_category)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category, parent, false)
        )
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        var category = list[position]
        holder.textView.text=category
        holder.itemView.setOnClickListener {
            categoryClickListener?.onCategoryClick(category)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun  setOnCategoryClickListener(categoryClickListener: CategoryClickListener){
        this.categoryClickListener=categoryClickListener
    }
    interface CategoryClickListener{
        fun onCategoryClick(categoryName: String)

    }
}


