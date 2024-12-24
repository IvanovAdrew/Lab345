package com.example.lab345unichnu.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab345unichnu.data.local.models.Device

@Dao
interface DeviceDao {
    @Query("SELECT * FROM device")
    fun getAll(): LiveData<List<Device>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(devices: List<Device>)
    @Query("SELECT * FROM device WHERE uid IN (:deviceIds)")
    fun loadAllByIds(deviceIds: IntArray): LiveData<List<Device>>
    @Delete
    suspend fun delete(device: Device)
}