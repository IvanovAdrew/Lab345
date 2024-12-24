package com.example.lab345unichnu.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab345unichnu.R
import com.example.lab345unichnu.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewmodel: MainViewmodel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CustomPhoneAdapter

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

        binding.mainRclView.setHasFixedSize(true)
        binding.mainRclView.layoutManager = LinearLayoutManager(this@MainActivity)

        mainViewmodel.phones.observe(this){ phones ->
            adapter = CustomPhoneAdapter(phones, this)
            binding.mainRclView.adapter = adapter
        }
        binding.btnRefresh.setOnClickListener {
            mainViewmodel.fetchAndStoreProducts()
        }
    }
}