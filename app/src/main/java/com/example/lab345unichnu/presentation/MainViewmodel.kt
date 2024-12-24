package com.example.lab345unichnu.presentation


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab345unichnu.repository.PhonesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(
    private val phonesRepository: PhonesRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    val phones = phonesRepository.getAllPhones()
    fun fetchAndStoreProducts() {
        viewModelScope.launch {
            phonesRepository.processAndStoreProducts(context)
        }
    }
}