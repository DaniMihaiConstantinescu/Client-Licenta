package com.example.testapp.utils.viewModels.scenes

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.dataClasses.general.GeneralDevice
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class SceneAddDeviceViewModel(
    private val devicesInScene: List<String>
): ViewModel() {

    var devices by mutableStateOf<List<GeneralDevice>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    val auth = Firebase.auth
    private var userId by mutableStateOf("")
    
    init {
        auth.currentUser?.run {
            userId = uid
        }
        
        viewModelScope.launch {
            try {
                isLoading = true
                val response = RetrofitClient.hubService.getAllDevicesNotIn(userId ,devicesInScene)
                // verify if it has scenes
                isLoading = false
                devices = response.devices ?: emptyList<GeneralDevice>()
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
                isLoading = false
            }
        }
    }
}