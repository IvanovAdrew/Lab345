package com.example.lab345unichnu.presentation


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.lab345unichnu.data.local.models.Device
import com.example.lab345unichnu.repository.PhonesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(
    private val phonesRepository: PhonesRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val phones = phonesRepository.getAllPhones()
    private var _uiStateFlow = MutableStateFlow<List<DeviceUI>>(emptyList())
    private var _uiCreateStateFlow = MutableStateFlow<List<DeviceUI?>>(emptyList())
    val uiStateFlow = _uiStateFlow
    val uiCreateStateFlow = _uiCreateStateFlow
    init {
        viewModelScope.launch {
        phones.asFlow().collect { devicesFromDb ->
                _uiStateFlow.update { currentUiList ->
                    devicesFromDb.map { device ->
                        val existing = currentUiList.find { it.device.uid == device.uid } //если девайс есть в коллекции его нужно обновить, если девайса нету в коллекции, его нужно добавить
                        println(existing?.isEditing)
                        DeviceUI(
                            device = device,
                            isEditing = existing?.isEditing?: false
                        )
                    }
                }
            }
        }
    }

//    val devicesState = phonesRepository.getAllPhones().asFlow()
//        .map { devices ->
//            devices.map {
//                DeviceUI(device = it)
//            }
//        }.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList()
//        )


    fun fetchAndStoreProducts() {
        viewModelScope.launch {
            phonesRepository.processAndStoreProducts(context)
        }
    }

    fun deleteProduct(deviceUI: DeviceUI) {
        viewModelScope.launch {
            phonesRepository.deleteProduct(deviceUI.device)
        }
    }

    fun startEditingProduct(previousDevice: DeviceUI) {

        //val currentDevices = devicesState.value.toMutableList()
        val index = _uiStateFlow.value.indexOfFirst { it.device.uid == previousDevice.device.uid }
        if (index != -1) {
            _uiStateFlow.update { currentUiList ->
                currentUiList.map { deviceUi ->
                    if(currentUiList.indexOf(deviceUi)==index){
                        DeviceUI(
                            isEditing = true,
                            device = deviceUi.device
                        )
                    } else{
                        deviceUi
                    }
                }
                }
            }
        }
    fun createNewCellOfProduct(){
        _uiCreateStateFlow.update { currentCreatingUiList ->
            var myList = currentCreatingUiList.toMutableList()
            if (currentCreatingUiList.isEmpty()) {
                myList.add(null)
            }
            myList
        }
    }
    fun stopEditingProduct(previousDevice: DeviceUI?) {
        if (previousDevice!=null) {

            //val currentDevices = devicesState.value.toMutableList()
            val index =
                _uiStateFlow.value.indexOfFirst { it.device.uid == previousDevice.device.uid }
            if (index != -1) {
                _uiStateFlow.update { currentUiList ->
                    currentUiList.map { deviceUi ->
                        if (currentUiList.indexOf(deviceUi) == index) {
                            DeviceUI(
                                isEditing = false,
                                device = deviceUi.device
                            )
                        } else {
                            deviceUi
                        }
                    }
                }
            }
        }
    }
    fun stopCreatingProduct(previousDevice: DeviceUI?) {
        _uiCreateStateFlow.update {
            emptyList()
        }
    }



    fun saveEditingProduct(newDeviceUI: DeviceUI) {
        viewModelScope.launch {
            phonesRepository.editDevice(newDeviceUI.device,context)
            stopEditingProduct(newDeviceUI)
        }
    }
    fun saveCreatingProduct(newDeviceUI: DeviceUI) {
        viewModelScope.launch {
            phonesRepository.insertPhone(newDeviceUI.device,context)
            stopCreatingProduct(newDeviceUI)
        }
    }
//    fun saveImageResource():String?{
//        viewModelScope.launch {
//            phonesRepository.saveImageToAppStorage(context, )
//        }
//    }
}