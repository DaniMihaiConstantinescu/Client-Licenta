package com.example.testapp.utils.dataClasses.homeScreen

import com.example.testapp.utils.dataClasses.general.Device

data class SceneToCreate(
    val devices: List<Device>,
    val isActive: Boolean,
    val sceneName: String,
)
