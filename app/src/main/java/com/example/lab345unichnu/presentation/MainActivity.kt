package com.example.lab345unichnu.presentation

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.lab345unichnu.R
import com.example.lab345unichnu.data.database.AppDatabase
import com.example.lab345unichnu.databinding.ActivityMainBinding
import com.example.lab345unichnu.data.model.Phone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var phones = ArrayList<Phone>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "phones"
        ).build()

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

        phones.add(Phone(1, namesOfPhones[0], uri1,priceOfPhones[0].toInt()))
        phones.add(Phone(2, namesOfPhones[1], uri2,priceOfPhones[1].toInt()))
        phones.add(Phone(3, namesOfPhones[2], uri3,priceOfPhones[2].toInt()))
        phones.add(Phone(4, namesOfPhones[3], uri4,priceOfPhones[3].toInt()))
        CoroutineScope(Dispatchers.IO).launch {
            db.phoneDao().insertAll(phones)
            val customAdapter = CustomAdapter(db.phoneDao().getAll(), this@MainActivity)
            CoroutineScope(Dispatchers.Main).launch {
                binding.mainRclView.setHasFixedSize(true)
                binding.mainRclView.layoutManager = LinearLayoutManager(this@MainActivity)
                binding.mainRclView.adapter = customAdapter
            }
        }


//        val customAdapter = CustomAdapter(db.phoneDao().getAll())
//
//        binding.mainRclView.setHasFixedSize(true)
//        binding.mainRclView.layoutManager = LinearLayoutManager(this)
//        binding.mainRclView.adapter = customAdapter


    }
    fun saveImageToAppStorage(context: Context, bitmap: Bitmap, fileName: String): String? {
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyAppImages")
        if (!directory.exists()) directory.mkdirs()

        val file = File(directory, fileName)
        return try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            file.absolutePath // Возвращаем путь к файлу
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


}