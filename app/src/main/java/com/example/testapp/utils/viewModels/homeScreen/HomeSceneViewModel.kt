package com.example.testapp.utils.viewModels.homeScreen

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

class HomeSceneViewModel: ViewModel(){

    var topScenes by mutableStateOf<List<Scene>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = RetrofitClient.sceneService.getTopScenes("1")
                // verify if it has scenes
                val scenes = response.scenes
                isLoading = false
                 topScenes = scenes ?: emptyList()
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
                isLoading = false
            }
        }
    }

    fun toggleScene(userId: String, sceneId: String){
        // call API
        return
    }

}



