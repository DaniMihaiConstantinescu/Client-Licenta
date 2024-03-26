package com.example.testapp.utils.viewModels.scenes

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SceneCardViewModel: ViewModel() {

    var toggledSuccesfully by mutableStateOf(false)
        private set

    fun toggleScene(sceneId: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.sceneService.toggleScene("1", sceneId)
                toggledSuccesfully = response.isSuccessful
                callback(toggledSuccesfully)
            } catch (e: HttpException) {
                handleHttpException(e)
                callback(false)
            } catch (e: Exception) {
                handleOtherException(e)
                callback(false)
            }
        }
    }

    private fun handleHttpException(exception: HttpException) {
        if (exception.code() == 404) {
            Log.e("TOGGLE SCENE ERROR", "Scene not found")
        } else {
            Log.e("TOGGLE SCENE ERROR", "HTTP Error: ${exception.code()}")
        }
    }private fun handleOtherException(exception: Exception) {
        Log.e("TOGGLE SCENE ERROR", "Server Error: ${exception.message}")
    }

}