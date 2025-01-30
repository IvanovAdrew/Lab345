package com.example.lab345unichnu.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import androidx.lifecycle.LiveData
import coil.Coil
import coil.request.ImageRequest
import com.example.lab345unichnu.data.local.models.Device
import com.example.lab345unichnu.data.local.DeviceDao
import com.example.lab345unichnu.data.remote.api.ProductsApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhonesRepository @Inject constructor(private val deviceDao: DeviceDao, private val productsApiService: ProductsApiService) {

    fun getAllPhones(): LiveData<List<Device>> = deviceDao.getAll()

    fun insertPhone(device: Device, context: Context){

        CoroutineScope(Dispatchers.IO).launch {
            device.image = device.image?.let { coilUrlToBitmap(context, it)?.let { saveBitmapImageToAppStorage(context = context, bitmap = it, device.name) } }
            deviceDao.insert(device)
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
    suspend fun deleteProduct(device: Device){
        CoroutineScope(Dispatchers.IO).launch {
            deviceDao.delete(device)
        }
    }
    suspend fun editDevice(device: Device, context: Context){

        CoroutineScope(Dispatchers.IO).launch {
            device.image = device.image?.let { coilUrlToBitmap(context, it)?.let { saveBitmapImageToAppStorage(context = context, bitmap = it, device.name) } }
            deviceDao.insert(device)
        }

    }
    private suspend fun downloadImage(context: Context, url: String): String? {
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

    suspend fun coilUrlToBitmap(context: Context, imageUrl: String): Bitmap? {
        return try {
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false) // Обязательно, иначе Bitmap будет null
                .build()

            val drawable = Coil.imageLoader(context).execute(request).drawable
            (drawable as? BitmapDrawable)?.bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveBitmapImageToAppStorage(context: Context, bitmap: Bitmap, name: String = ""): String? {
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyAppImages")
        if (!directory.exists()) directory.mkdirs()

        val fileName = "${name}_image_${System.currentTimeMillis()}.png" // Унікальне ім'я
        val file = File(directory, fileName)

        return try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


}