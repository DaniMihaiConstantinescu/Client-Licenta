package com.example.testapp.utils.viewModels.homeScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.testapp.utils.dataClasses.general.Device
import com.example.testapp.utils.dataClasses.general.Settings
import com.example.testapp.utils.dataClasses.homeScreen.Scene

class HomeSceneViewModel: ViewModel(){

    private val testScenes = listOf(
        Scene(
            devices = listOf(
                Device(
                    macAddress = "addressamac1",
                    settings = Settings(
                        listOf(
                            Pair("temeperature", "22,5"),
                            Pair("otherSetting", "value1")
                        )
                    )
                )
            ),
            isActive = true,
            sceneId = "-NsiYEZFVGx__HaZOY5i1",
            sceneName = "Scene 1"
        ),
        Scene(
            devices = listOf(
                Device(
                    macAddress = "addressamac2",
                    settings = Settings(
                        listOf(
                            Pair("temperature", "23.5"),
                            Pair("otherSetting", "value2")
                        )
                    )
                )
            ),
            isActive = false,
            sceneId = "-NsiYEZFVGx__HaZOY5i2",
            sceneName = "Scene 2"
        )
    )

    var topScenes by mutableStateOf<List<Scene>>(testScenes)
        private set

    fun toggleScene(userId: String, sceneId: String){
        // call API
        return
    }

}



