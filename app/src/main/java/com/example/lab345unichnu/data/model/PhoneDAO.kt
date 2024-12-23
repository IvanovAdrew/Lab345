package com.example.lab345unichnu.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhoneDAO {
    @Query("SELECT * FROM phone")
    fun getAll(): List<Phone>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(phones: List<Phone>)
    @Query("SELECT * FROM phone WHERE uid IN (:phoneIds)")
    fun loadAllByIds(phoneIds: IntArray): List<Phone>
    @Delete
    fun delete(phone: Phone)
}