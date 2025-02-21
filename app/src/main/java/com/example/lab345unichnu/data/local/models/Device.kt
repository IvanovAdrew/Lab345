package com.example.lab345unichnu.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Device(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo (name = "name_of_phone")
    var name: String,
    @ColumnInfo (name = "link_to_the_image")
    var image: String?,
    @ColumnInfo(name = "type_of_device")
    var type: String?
)
