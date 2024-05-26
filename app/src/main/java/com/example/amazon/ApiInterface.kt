package com.example.amazon

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
    @GET("products")
    fun getproductsById(@Query("id") id: String):Call<ProductsResponseArr>

//    @GET("user")
//    fun getUser(): Call<UserResponse>
//    @GET("cart")
//    fun getCart(): Call<CartResponse>
//    @GET("products")
//    fun getProducts():Call<ProductsResponse>
//
//    @GET("user")
//    fun getUserLimit(@Query("limit") limit: Int): Call<UserResponse>
//    @GET("cart")
//    fun getCartLimit(@Query("limit") limit: Int): Call<CartResponse>
//    @GET("products")
//    fun getProductsLimit(@Query("limit") limit: Int): Call<ProductsResponse>
//
//    @POST("user")
//    fun insertUser(@Body userRequest: UserRequest) :Call<UserResponse>
//    @POST("cart")
//    fun insertCart(@Body cartRequest: CartRequest) :Call<CartResponse>
//    @POST("products")
//    fun insertProducts(@Body productsRequest: ProductsRequest) :Call<ProductsResponse>
//
//    @PUT("user/{id}")
//    fun updateUser(@Path("id") id: Int, @Body userRequest: UserRequest) :Call<UserResponse>
//    @PUT("cart/{id}")
//    fun updateCart(@Path("id") id: Int, @Body cartRequest: CartRequest) :Call<CartResponse>
//    @PUT("products/{id}")
//    fun updateProducts(@Path("id") id: Int, @Body productsRequest: ProductsRequest) :Call<ProductsRequest>
//
//    @DELETE("user/{id}")
//    fun deleteUser(@Path("id") id: Int) :Call<DeleteUserResponse>
//    @DELETE("cart/{id}")
//    fun deleteCart(@Path("id") id: Int) :Call<DeleteCartResponse>
//    @DELETE("products/{id}")
//    fun deleteProducts(@Path("id") id: Int) :Call<DeleteProductsReponse>
}