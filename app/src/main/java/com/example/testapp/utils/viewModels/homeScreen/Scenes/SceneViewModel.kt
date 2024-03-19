package com.example.testapp.utils.viewModels.homeScreen.Scenes

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.dataClasses.general.Device
import com.example.testapp.utils.dataClasses.homeScreen.Scene
import kotlinx.coroutines.launch
import retrofit2.HttpException

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
    fun addDeviceToScene(newDevice: Device) {
        viewModelScope.launch {
            try {
                // Make the API call to add the device to the scene
                RetrofitClient.sceneService.addDeviceToScene("1", sceneId, newDevice)
                val updatedScene = scene.copy(devices = scene.devices + newDevice)
                scene = updatedScene
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    Log.e("ADD DEVICE ERROR", "Device not found")
                } else {
                    Log.e("ADD DEVICE ERROR", "HTTP Error")
                }
            } catch (e: Exception) {
                Log.e("ADD DEVICE ERROR", "Server Error")
            }
        }
    }
    fun deleteDevice(deviceId: String){
        viewModelScope.launch {
            try {
                RetrofitClient.sceneService.deleteDeviceFromScene("1", sceneId, deviceId)
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    Log.e("ADD DEVICE ERROR", "Device not found")
                } else {
                    Log.e("ADD DEVICE ERROR", "HTTP Error")
                }
            } catch (e: Exception) {
                Log.e("ADD DEVICE ERROR", "Server Error")
            }
        }
    }

}