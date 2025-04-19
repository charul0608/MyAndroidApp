package com.example.myandroidapp.data.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.restful-api.dev/"

    val retrofit: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun create(): ApiService {
        return retrofit
    }

    // Method to delete an item
    suspend fun deleteItem(itemId: String): Response<Unit> {
        return retrofit.deleteItem(itemId)
    }
}