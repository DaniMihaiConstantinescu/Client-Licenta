package com.example.testapp.utils.viewModels.homeScreen.Scenes

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.dataClasses.homeScreen.Scene
import kotlinx.coroutines.launch

class SceneViewModel(
    private val sceneId: String
): ViewModel() {

    var scene by mutableStateOf(Scene(devices = emptyList(), isActive = false, sceneId = "", sceneName = ""))
        private set
    var isLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = RetrofitClient.sceneService.getScene("1", sceneId)
                // verify if it has scenes
                isLoading = false
                scene = response.scene ?: Scene(devices = emptyList(), isActive = false, sceneId = "", sceneName = "")
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
                isLoading = false
            }
        }
    }

    fun updateName(newName: String){

    }
    fun addDevice(deviceId: String){

    }
    fun deleteDevice(deviceId: String){

    }

}