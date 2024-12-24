package com.example.lab345unichnu.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import com.example.lab345unichnu.data.local.models.Device
import com.example.lab345unichnu.data.local.DeviceDao
import com.example.lab345unichnu.data.remote.api.ProductsApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhonesRepository @Inject constructor(private val deviceDao: DeviceDao, private val productsApiService: ProductsApiService) {

    fun getAllPhones(): LiveData<List<Device>> = deviceDao.getAll()

    fun insertPhone(device: Device){
        CoroutineScope(Dispatchers.IO).launch {
            deviceDao.insertAll(listOf(device))
        }
    }

    suspend fun processAndStoreProducts(
        context: Context
    ) {
        try {
            // Запит даних з API
            val response = productsApiService.getProducts(
                query = "xiaomi redmi",
                page = 1,
                apiKey = "dbc88a2a17msh401ceebc41b8101p141d93jsn91a3e5f08e24"
            )

            // Обробка кожного продукту
            val devices = response.products.mapNotNull { product ->
                val imagePath = product.images.firstOrNull()?.let { downloadImage(context, it) }
                if (imagePath != null) {
                    Device(
                        uid = product.id.toInt(),
                        name = product.name,
                        image = imagePath,
                        type = product.categories.firstOrNull()?.name
                    )
                } else null
            }

            // Збереження у Room
            deviceDao.insertAll(devices)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    suspend fun downloadImage(context: Context, url: String): String? {
        return try {
            val bitmap = withContext(Dispatchers.IO) {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.inputStream.use { BitmapFactory.decodeStream(it) }
            }
            val file = File(context.filesDir, "${UUID.randomUUID()}.jpg")
            FileOutputStream(file).use {
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}