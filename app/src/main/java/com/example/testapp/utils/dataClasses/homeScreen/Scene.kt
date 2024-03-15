package com.example.testapp.utils.dataClasses.homeScreen

import com.example.testapp.utils.dataClasses.general.Device

data class Scene(
    val devices: List<Device>,
    val isActive: Boolean,
    val sceneId: String,
    val sceneName: String
)