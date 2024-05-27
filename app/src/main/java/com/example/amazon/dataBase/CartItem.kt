package com.example.amazon.dataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "cartTable")
data class CartItem(

    @PrimaryKey(autoGenerate = true) val cartId: Int,
    @ColumnInfo(name = "cart_user_id") val cartUserId: String,
//    @ColumnInfo(name = "cart_products") val products: ArrayList<CartProduct>
    val productId: Int,
    var quantity: Int,
    var price: Double,
    var totalPrice: Double,
    var image: String?,
    var title: String?,

) {





//    fun getFullDateString(): String {
//        val format = SimpleDateFormat("MMM dd, yyyy", Locale.US)
//        return format.format(Date(this.date))
//
//    }


    fun increaseQuantityOfProduct(productId: Int): CartItem {
        this.quantity = this.quantity.plus(1)
        return this
    }

    fun getQuantityOfProduct(): Int {
        return this.quantity
    }

    fun getTotalPriceOfOneProduct(): Double {
        return this.totalPrice
    }

    fun decreaseQuantityOfProduct(): CartItem {
        this.quantity = this.quantity.minus(1)
        return this
    }


}

