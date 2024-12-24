package com.example.lab345unichnu.data.remote.api

import com.example.lab345unichnu.data.remote.models.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ProductsApiService {
    @GET("api/products")
    suspend fun getProducts(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("lang") lang: String = "en",
        @Header("x-rapidapi-key") apiKey: String,
        @Header("x-rapidapi-host") apiHost: String = "products-database.p.rapidapi.com"
    ): ProductResponse // Повертає об'єкт відповіді
}