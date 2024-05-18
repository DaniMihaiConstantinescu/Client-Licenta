package com.example.testapp.utils.viewModels.hubs

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

class HubDevicesViewModel: ViewModel() {

    var allDevices by mutableStateOf<List<GeneralDevice>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    val auth = Firebase.auth
    private var userId by mutableStateOf("")

    fun getDevices(devicesList: List<String>){

//        auth.currentUser?.run {
//            userId = uid
//        }

        viewModelScope.launch {
            try {
                isLoading = true
//                val response = RetrofitClient.sceneService.getAllScenes(userId)
                val response = RetrofitClient.deviceService.getDevicesInList(devicesList)
                // verify if it has scenes
                val hubs = response.devices
                isLoading = false
                allDevices = hubs ?: emptyList()
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
                isLoading = false
            }
        }
    }

}