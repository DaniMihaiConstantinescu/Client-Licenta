package com.example.testapp.utils.viewModels.scenes

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.dataClasses.general.Device
import com.example.testapp.utils.dataClasses.general.GeneralDevice
import com.example.testapp.utils.dataClasses.homeScreen.Scene
import com.example.testapp.utils.dataClasses.homeScreen.SceneToCreate
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class AddSceneViewModel: ViewModel() {

    var scene by mutableStateOf(Scene(devices = emptyList(), isActive = false, sceneId = "", sceneName = ""))
        private set
    var devicesToAdd by mutableStateOf(emptyList<GeneralDevice>())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var responseCode by mutableStateOf<Response<Void>?>(null)
        private set

    val auth = Firebase.auth
    private var userId by mutableStateOf("")
    
    init {
        auth.currentUser?.run {
            userId = uid
        }
    }

    fun getAllDevicesFromHub(){
        viewModelScope.launch {
            devicesToAdd = getAllDevicesFromHubAsync()
        }
    }

    suspend fun getAllDevicesFromHubAsync(): List<GeneralDevice> {
        return try {
            isLoading = true
            val response = RetrofitClient.hubService.getAllDevices(userId)
            // verify if it has scenes
            isLoading = false
            response.devices
        } catch (e: Exception) {
            // Handle network errors
            Log.e("API Request", "Error: ${e.message}", e)
            emptyList()
        }
    }
    fun addDeviceToScene(newDevice: Device) {
        val updatedScene = scene.copy(devices = scene.devices + newDevice)
        scene = updatedScene
    }
    fun deleteDevice(deviceId: String) {
        val updatedDevices = scene.devices.filter { it.macAddress != deviceId }
        scene = scene.copy(devices = updatedDevices)
    }

    fun createScene(sceneName: String, callback: (Response<Void>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.sceneService.createScene(
                    userId,
                    SceneToCreate(
                        scene.devices,
                        scene.isActive,
                        sceneName
                    )
                )
                Log.e("CREATE SCENE ERROR", response.toString())
                responseCode = response
                callback(response) // Invoke the callback with the response
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("API Request", "Error: ${e.message}", e)
                callback(Response.error(500, ResponseBody.create(null, "Internal Server Error"))) // Return error response
            }
        }
    }


}