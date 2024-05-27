package com.example.amazon.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartDao {


    @Query("SELECT * FROM cartTable WHERE cart_user_id = :user_id")
    fun getCartItemsOfCurrentUserByUserId(user_id: String):List<CartItem>

    @Query("SELECT productId FROM cartTable WHERE cart_user_id = :user_id")
    fun getListOfProductsIdsOfCurrentUserById(user_id: String):List<Int>
    @Query("SELECT * FROM cartTable WHERE productId = :productId")
    fun getProductById(productId: Int) : CartItem

    @Query("DELETE  FROM cartTable WHERE cart_user_id = :userId AND productId = :productId")
    fun deleteProductFromCart(userId: String , productId: Int)


    @Query("SELECT * FROM cartTable ")
    fun getAllCarts(): List<CartItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductToCart(cartItem: CartItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCartItem(cartItem: CartItem)

    @Delete
    fun removeCartItem(cartItem: CartItem)


}