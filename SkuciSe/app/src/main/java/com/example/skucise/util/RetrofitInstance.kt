package com.example.skucise.util

import com.example.skucise.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

import com.google.gson.Gson


var gson = GsonBuilder()
    .setLenient()
    .create()

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    val api: API by lazy{
        retrofit.create(API::class.java)
    }
}