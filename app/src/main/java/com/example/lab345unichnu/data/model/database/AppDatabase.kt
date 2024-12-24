package com.example.lab345unichnu.data.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab345unichnu.data.model.Phone
import com.example.lab345unichnu.data.model.PhoneDAO

@Database(entities = [Phone::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun phoneDao(): PhoneDAO
}