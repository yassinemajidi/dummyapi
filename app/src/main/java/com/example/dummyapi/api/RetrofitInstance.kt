package com.example.dummyapi.api

import com.example.dummyapi.utils.Constants.Companion.url
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// init Retrofit Instance
object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val api: Api by lazy {
        retrofit.create(Api::class.java)
    }
}