package com.example.testapp.utils.dataClasses.homeScreen

import com.example.testapp.utils.dataClasses.general.Device

data class SceneResponse(
    val message: String,
    val scenes: List<Scene>
)
data class Scene(
    val devices: List<Device>,
    val isActive: Boolean,
    val sceneId: String,
    val sceneName: String
)


