package com.example.testapp.utils.viewModels.homeScreen.Schedules

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.dataClasses.general.GeneralDevice
import kotlinx.coroutines.launch

class ScheduleAddDeviceViewModel(
    private val devicesInSchedule: List<String>
): ViewModel() {

    var devices by mutableStateOf<List<GeneralDevice>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = RetrofitClient.deviceService.getDevicesNotInList(devicesInSchedule)
                // verify if it has schedules
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