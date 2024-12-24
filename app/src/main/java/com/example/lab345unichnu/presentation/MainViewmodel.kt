package com.example.lab345unichnu.presentation


import androidx.lifecycle.ViewModel
import com.example.lab345unichnu.repository.PhonesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(private val phonesRepository: PhonesRepository): ViewModel() {
    val phones = phonesRepository.getAllPhones()
}