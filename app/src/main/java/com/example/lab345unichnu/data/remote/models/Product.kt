package com.example.lab345unichnu.data.remote.models

data class Product(
    val id: String,
    val name: String,
    val images: List<String>,
    val categories: List<Category>
)