package com.example.testapp.utils.viewModels.homeScreen.Schedules

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.dataClasses.general.Device
import com.example.testapp.utils.dataClasses.homeScreen.Schedule
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ScheduleViewModel(
    private val scheduleId: String
): ViewModel() {

    var schedule by mutableStateOf(Schedule(
        devices = emptyList(),
        isActive = false,
        scheduleId = "",
        scheduleName = "",
        days = emptyList(),
        from = "",
        until = ""
    ))
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
                val response = RetrofitClient.scheduleService.getSchedule(userId, scheduleId)
                // verify if it has schedules
                isLoading = false
                schedule = response.schedule ?: Schedule(devices = emptyList(), isActive = false, scheduleId = "", scheduleName = "", days = emptyList(), from = "", until = "")
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
                isLoading = false
            }
        }
    }

    fun addDeviceToSchedule(newDevice: Device) {
        viewModelScope.launch {
            try {
                // Make the API call to add the device to the schedule
                val response = RetrofitClient.scheduleService.addDeviceToSchedule(userId, scheduleId, newDevice)
                if (response.isSuccessful) {
                    val updatedSchedule = schedule.copy(devices = schedule.devices + newDevice)
                    schedule = updatedSchedule
                } else {
                    Log.e("ADD DEVICE ERROR", "HTTP Error: ${response.code()}")
                }
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    Log.e("ADD DEVICE ERROR", "Device not found")
                } else {
                    Log.e("ADD DEVICE ERROR", "HTTP Error")
                }
            } catch (e: Exception) {
                Log.e("ADD DEVICE ERROR", "Server Error")
            }
        }
    }
    fun deleteDevice(deviceId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.scheduleService.deleteDeviceFromSchedule(userId, scheduleId, deviceId)
                if (response.isSuccessful) {
                    // Remove the device from the list
                    val updatedDevices = schedule.devices.filter { it.macAddress != deviceId }
                    schedule = schedule.copy(devices = updatedDevices)
                } else {
                    Log.e("DELETE DEVICE ERROR", "HTTP Error: ${response.code()}")
                }
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    Log.e("DELETE DEVICE ERROR", "Device not found")
                } else {
                    Log.e("DELETE DEVICE ERROR", "HTTP Error")
                }
            } catch (e: Exception) {
                Log.e("DELETE DEVICE ERROR", "Server Error")
            }
        }
    }


}