package com.example.myandroidapp.data.api

import com.example.myandroidapp.data.db.ApiObjectDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("objects")
    suspend fun getObjects(): Response<List<ApiObjectDto>>

    @DELETE("objects/{id}")
    suspend fun deleteItem(@Path("id") id: String): Response<Unit>

}