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
import retrofit2.HttpException

class AllScenesViewModel: ViewModel() {

    var allScenes by mutableStateOf<List<Scene>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = RetrofitClient.sceneService.getAllScenes("1")
                // verify if it has scenes
                val scenes = response.scenes
                isLoading = false
                allScenes = scenes ?: emptyList()
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
                isLoading = false
            }
        }
    }

    fun deleteScene(sceneId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.sceneService.deleteScene("1", sceneId)
                if (response.isSuccessful) {
                    allScenes = allScenes.filter { it.sceneId != sceneId }
                } else {
                    Log.e("DELETE SCENE ERROR", "HTTP Error: ${response.code()}")
                }
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    Log.e("DELETE SCENE ERROR", "Scene not found")
                } else {
                    Log.e("DELETE SCENE ERROR", "HTTP Error")
                }
            } catch (e: Exception) {
                Log.e("DELETE SCENE ERROR", "Server Error")
            }
        }
    }


}