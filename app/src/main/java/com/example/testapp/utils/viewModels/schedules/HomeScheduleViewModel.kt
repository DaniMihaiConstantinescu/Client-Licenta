package com.example.testapp.utils.viewModels.homeScreen.Schedules

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.dataClasses.homeScreen.Schedule
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class HomeScheduleViewModel: ViewModel(){

    var topSchedules by mutableStateOf<List<Schedule>>(emptyList())
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
                val response = RetrofitClient.scheduleService.getTopSchedules(userId)
                // verify if it has scenes
                val schedules = response.schedules
                topSchedules = schedules ?: emptyList()
                isLoading = false
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
                isLoading = false
            }
        }
    }

    fun toggleSchedule(userId: String, scheduleId: String){
        // call API
        return
    }

}



