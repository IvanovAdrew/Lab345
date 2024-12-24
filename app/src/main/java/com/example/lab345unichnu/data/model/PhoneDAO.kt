package com.example.lab345unichnu.data.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhoneDAO {
    @Query("SELECT * FROM phone")
    fun getAll(): LiveData<List<Phone>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(phones: List<Phone>)
    @Query("SELECT * FROM phone WHERE uid IN (:phoneIds)")
    fun loadAllByIds(phoneIds: IntArray): LiveData<List<Phone>>
    @Delete
    suspend fun delete(phone: Phone)
}