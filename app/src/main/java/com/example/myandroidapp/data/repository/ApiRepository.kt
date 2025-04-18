package com.example.myandroidapp.data.repository

import com.example.myandroidapp.data.api.ApiClient
import com.example.myandroidapp.data.db.ApiObjectDao
import com.example.myandroidapp.data.db.ApiObjectEntity

class ApiRepository(private val dao: ApiObjectDao) {

    suspend fun fetchAndStoreObjects(): Boolean {
        val response = ApiClient.retrofit.getObjects()
        return if (response.isSuccessful) {
            val objects = response.body()?.map {
                val dataString = it.data?.map { (k, v) -> "$k: $v" }?.joinToString(", ") ?: "N/A"
                ApiObjectEntity(it.id, it.name, dataString)
            } ?: emptyList()
            dao.insertAll(objects)
            true
        } else {
            false
        }
    }

    suspend fun getAllObjects(): List<ApiObjectEntity> = dao.getAll()

    suspend fun updateObject(entity: ApiObjectEntity) = dao.update(entity)

    suspend fun deleteObject(entity: ApiObjectEntity) = dao.delete(entity)
}