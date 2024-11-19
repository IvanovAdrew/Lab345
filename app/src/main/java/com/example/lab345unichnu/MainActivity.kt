package com.example.lab345unichnu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab345unichnu.databinding.ActivityMainBinding

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

        val namesOfPhones = resources.getStringArray(R.array.smartphones_names_full)
        val priceOfPhones = resources.getStringArray(R.array.price_of_phones)

        phones.add(Phone(namesOfPhones[0],R.drawable.meizu_pro_7_plus,priceOfPhones[0].toInt()))
        phones.add(Phone(namesOfPhones[1],R.drawable.huawei,priceOfPhones[1].toInt()))
        phones.add(Phone(namesOfPhones[2],R.drawable.google_pixel_last,priceOfPhones[2].toInt()))
        phones.add(Phone(namesOfPhones[3],R.drawable.iphone,priceOfPhones[3].toInt()))

        val customAdapter = CustomAdapter(phones)

        binding.mainRclView.setHasFixedSize(true)
        binding.mainRclView.layoutManager = LinearLayoutManager(this)
        binding.mainRclView.adapter = customAdapter


    }
}