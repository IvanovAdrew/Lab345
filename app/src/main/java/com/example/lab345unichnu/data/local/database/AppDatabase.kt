package com.example.lab345unichnu.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab345unichnu.data.local.models.Device
import com.example.lab345unichnu.data.local.DeviceDao

@Database(entities = [Device::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun phoneDao(): DeviceDao
}