package com.example.testapp.utils.viewModels.hubs

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.dataClasses.hubScreen.Hub
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class AllHubsViewModel: ViewModel() {

    var allHubs by mutableStateOf<List<Hub>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    val auth = Firebase.auth
    private var userId by mutableStateOf("")

    init {
//        auth.currentUser?.run {
//            userId = uid
//        }

        viewModelScope.launch {
            try {
                isLoading = true
//                val response = RetrofitClient.sceneService.getAllScenes(userId)
                val response = RetrofitClient.hubService.getAllHubs("1")
                // verify if it has scenes
                val hubs = response.hubs
                isLoading = false
                allHubs = hubs ?: emptyList()
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
                isLoading = false
            }
        }
    }



}