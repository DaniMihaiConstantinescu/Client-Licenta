package com.example.testapp.utils.viewModels.homeScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.dataClasses.general.Device
import com.example.testapp.utils.dataClasses.homeScreen.Schedule
import kotlinx.coroutines.launch

class HomeScheduleViewModel: ViewModel(){

    var topSchedules by mutableStateOf<List<Schedule>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = RetrofitClient.scheduleService.getTopSchedules("1")
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



