package com.example.myandroidapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "api_items")
data class ApiItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val data: String?
)
