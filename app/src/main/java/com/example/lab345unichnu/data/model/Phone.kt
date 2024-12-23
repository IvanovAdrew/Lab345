package com.example.lab345unichnu.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Phone(
    @PrimaryKey
    val uid: Int,
    @ColumnInfo (name = "name_of_phone")
    val name: String,
    @ColumnInfo (name = "link_to_the_image")
    val image: String?,
    @ColumnInfo(name = "price_of_phone")
    val price: Int?
)
