package com.example.myandroidapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ApiObjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(objects: List<ApiObjectEntity>)

    @Query("SELECT * FROM api_objects")
    suspend fun getAll(): List<ApiObjectEntity>

    @Update
    suspend fun update(apiObjectEntity: ApiObjectEntity): Int

    @Delete
    suspend fun delete(apiObjectEntity: ApiObjectEntity): Int
}
