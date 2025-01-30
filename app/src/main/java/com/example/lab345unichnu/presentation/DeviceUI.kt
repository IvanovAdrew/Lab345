package com.example.lab345unichnu.presentation

import com.example.lab345unichnu.data.local.models.Device

data class DeviceUI(
    val device: Device,
    var isEditing: Boolean = false,
    var isCreating: Boolean = false
){

}
