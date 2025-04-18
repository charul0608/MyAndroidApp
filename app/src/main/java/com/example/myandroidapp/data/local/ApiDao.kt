package com.example.myandroidapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myandroidapp.data.local.ApiItemEntity

@Dao
interface ApiDao {

    @Query("SELECT * FROM api_items")
    fun getAllItems(): LiveData<List<ApiItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ApiItemEntity>)

    @Update
    suspend fun updateItem(item: ApiItemEntity)

    @Delete
    suspend fun deleteItem(item: ApiItemEntity)
}
