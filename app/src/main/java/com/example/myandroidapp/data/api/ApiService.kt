package com.example.myandroidapp.data.api

import com.example.myandroidapp.data.model.ApiObject
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("objects")
    suspend fun getObjects(): Response<List<ApiObject>>
}