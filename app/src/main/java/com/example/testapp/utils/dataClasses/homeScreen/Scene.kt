package com.example.testapp.utils.dataClasses.homeScreen

import com.example.testapp.utils.dataClasses.general.Device

data class Scene(
    var devices: List<Device>,
    var isActive: Boolean,
    var sceneId: String,
    var sceneName: String
)