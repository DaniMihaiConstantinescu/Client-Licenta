package com.example.testapp.utils.viewModels.scenes

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

    fun addDeviceToScene(newDevice: Device) {
        viewModelScope.launch {
            try {
                // Make the API call to add the device to the scene
                val response = RetrofitClient.sceneService.addDeviceToScene("1", sceneId, newDevice)
                if (response.isSuccessful) {
                    val updatedScene = scene.copy(devices = scene.devices + newDevice)
                    scene = updatedScene
                } else {
                    Log.e("ADD DEVICE ERROR", "HTTP Error: ${response.code()}")
                }
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
    fun deleteDevice(deviceId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.sceneService.deleteDeviceFromScene("1", sceneId, deviceId)
                if (response.isSuccessful) {
                    // Remove the device from the list
                    val updatedDevices = scene.devices.filter { it.macAddress != deviceId }
                    scene = scene.copy(devices = updatedDevices)
                } else {
                    Log.e("DELETE DEVICE ERROR", "HTTP Error: ${response.code()}")
                }
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    Log.e("DELETE DEVICE ERROR", "Device not found")
                } else {
                    Log.e("DELETE DEVICE ERROR", "HTTP Error")
                }
            } catch (e: Exception) {
                Log.e("DELETE DEVICE ERROR", "Server Error")
            }
        }
    }


}