package com.example.lab345unichnu

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.example.lab345unichnu.data.model.Phone
import com.example.lab345unichnu.repository.PhonesRepository
import dagger.hilt.android.HiltAndroidApp
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltAndroidApp
class MyApplication: Application() {

    @Inject lateinit var phonesRepository: PhonesRepository

    override fun onCreate() {
        super.onCreate()
        if (isFirstLaunch()) {
            addDefaultPhonesToDatabase()
        }
    }

    private fun addDefaultPhonesToDatabase() {
        val namesOfPhones = resources.getStringArray(R.array.smartphones_names_full)
        val priceOfPhones = resources.getStringArray(R.array.price_of_phones)

        val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.meizu_pro_7_plus)
        val uri1 = saveImageToAppStorage(this, bitmap1, "meizu_pro_7_plus.png")
        val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.huawei)
        val uri2 = saveImageToAppStorage(this, bitmap2, "huawei.png")
        val bitmap3 = BitmapFactory.decodeResource(resources, R.drawable.google_pixel_last)
        val uri3 = saveImageToAppStorage(this, bitmap3, "google_pixel_last.png")
        val bitmap4 = BitmapFactory.decodeResource(resources, R.drawable.iphone)
        val uri4 = saveImageToAppStorage(this, bitmap4, "iphone.png")

        phonesRepository.insertPhone(Phone(1, namesOfPhones[0], uri1,priceOfPhones[0].toInt()))
        phonesRepository.insertPhone(Phone(2, namesOfPhones[1], uri2,priceOfPhones[1].toInt()))
        phonesRepository.insertPhone(Phone(3, namesOfPhones[2], uri3,priceOfPhones[2].toInt()))
        phonesRepository.insertPhone(Phone(4, namesOfPhones[3], uri4,priceOfPhones[3].toInt()))
    }

    private fun isFirstLaunch(): Boolean {
        // Перевірка, чи перший запуск
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isFirstLaunch = sharedPreferences.getBoolean("is_first_launch", true)
        if (isFirstLaunch) {
            sharedPreferences.edit().putBoolean("is_first_launch", false).apply()
            return true
        }
        return false
    }

    private fun saveImageToAppStorage(context: Context, bitmap: Bitmap, fileName: String): String? {
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyAppImages")
        if (!directory.exists()) directory.mkdirs()

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