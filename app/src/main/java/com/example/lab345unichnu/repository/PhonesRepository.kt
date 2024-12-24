package com.example.lab345unichnu.repository

import androidx.lifecycle.LiveData
import com.example.lab345unichnu.data.model.Phone
import com.example.lab345unichnu.data.model.PhoneDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhonesRepository @Inject constructor(private val phoneDao: PhoneDAO) {

    fun getAllPhones(): LiveData<List<Phone>> = phoneDao.getAll()

    fun insertPhone(phone: Phone){
        CoroutineScope(Dispatchers.IO).launch {
            phoneDao.insertAll(listOf(phone))
        }
    }
}