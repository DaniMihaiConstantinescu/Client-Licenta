package com.example.testapp.utils.viewModels.homeScreen.Schedules

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.dataClasses.homeScreen.Schedule
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AllSchedulesViewModel: ViewModel() {

    var allSchedules by mutableStateOf<List<Schedule>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = RetrofitClient.scheduleService.getAllSchedules("1")
                // verify if it has schedules
                val schedules = response.schedules
                isLoading = false
                allSchedules = schedules ?: emptyList()
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
                isLoading = false
            }
        }
    }

    fun deleteSchedule(scheduleId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.scheduleService.deleteSchedule("1", scheduleId)
                if (response.isSuccessful) {
                    allSchedules = allSchedules.filter { it.scheduleId != scheduleId }
                } else {
                    Log.e("DELETE SCHEDULE ERROR", "HTTP Error: ${response.code()}")
                }
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    Log.e("DELETE SCHEDULE ERROR", "Schedule not found")
                } else {
                    Log.e("DELETE SCHEDULE ERROR", "HTTP Error")
                }
            } catch (e: Exception) {
                Log.e("DELETE SCHEDULE ERROR", "Server Error")
            }
        }
    }


}