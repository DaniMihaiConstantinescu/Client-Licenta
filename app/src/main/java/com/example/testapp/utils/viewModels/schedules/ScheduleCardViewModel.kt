package com.example.testapp.utils.viewModels.homeScreen.Schedules

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ScheduleCardViewModel: ViewModel() {

    var toggledSuccesfully by mutableStateOf(false)
        private set

    val auth = Firebase.auth
    private var userId by mutableStateOf("")
    
    init {
        auth.currentUser?.run {
            userId = uid
        }
    }

    fun toggleSchedule(scheduleId: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.scheduleService.toggleSchedule(userId, scheduleId)
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
            Log.e("TOGGLE SCHEDULE ERROR", "Schedule not found")
        } else {
            Log.e("TOGGLE SCHEDULE ERROR", "HTTP Error: ${exception.code()}")
        }
    }private fun handleOtherException(exception: Exception) {
        Log.e("TOGGLE SCHEDULE ERROR", "Server Error: ${exception.message}")
    }
    
}