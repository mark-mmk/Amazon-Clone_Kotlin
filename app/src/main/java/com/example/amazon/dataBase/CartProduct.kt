package com.example.amazon.dataBase

data class CartProduct(
    val productId: Int,
    var quantity: Int,
    var price: Double,
    var totalPrice: Double,
    var image: String?,
    var title: String?,
    var date: Long,

)