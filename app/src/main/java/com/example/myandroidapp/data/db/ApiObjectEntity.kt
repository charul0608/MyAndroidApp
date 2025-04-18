package com.example.myandroidapp.data.db

import androidx.room.*
import com.example.myandroidapp.data.model.ApiObject

@Entity(tableName = "api_objects")
data class ApiObjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val data: String // Stored as a JSON string or comma-separated list
)