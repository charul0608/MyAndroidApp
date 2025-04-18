package com.example.myandroidapp.data.api

import com.example.myandroidapp.data.db.ApiObjectDto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("objects")
    suspend fun getObjects(): Response<List<ApiObjectDto>>
}