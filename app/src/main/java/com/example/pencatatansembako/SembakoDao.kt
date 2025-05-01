package com.example.pencatatansembako

import androidx.room.*

@Dao
interface SembakoDao {
    @Query("SELECT * FROM Sembako")
    suspend fun getAll(): List<Sembako>

    @Insert
    suspend fun insert(sembako: Sembako)

    @Delete
    suspend fun delete(sembako: Sembako)
}
