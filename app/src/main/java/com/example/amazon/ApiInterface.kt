package com.example.amazon

import com.example.amazon.products.ProductResponseItem
import com.example.amazon.products.ProductsResponseArr
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("products")
    fun getAllProducts(): Call<ProductsResponseArr>
    @GET("products")
    fun getLimitProducts(@Query("limit") limit: Int): Call<ProductsResponseArr>
    @GET("products/categories")
    fun getAllCategories(): Call<ArrayList<String>>
    @GET("products/category/{categoryName}")
    fun  getSingleCategory(@Path("categoryName") categoryName: String): Call<ProductsResponseArr>
    @GET("products/{id}")
    fun getProductById(@Path("id") id: Int):Call<ProductResponseItem>

}