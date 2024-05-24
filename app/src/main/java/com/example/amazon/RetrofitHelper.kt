package com.example.amazon

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {

    companion object{
        private val baseURL="https://fakestoreapi.com/"
        fun getInstance():ApiInterface{
            return Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL)
                .build()
                .create(ApiInterface::class.java)
        }
    }
}