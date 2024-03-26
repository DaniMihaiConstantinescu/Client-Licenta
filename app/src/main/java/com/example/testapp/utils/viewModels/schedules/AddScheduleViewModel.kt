package com.example.testapp.utils.viewModels.homeScreen.Schedules

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.dataClasses.general.Device
import com.example.testapp.utils.dataClasses.general.GeneralDevice
import com.example.testapp.utils.dataClasses.homeScreen.Schedule
import com.example.testapp.utils.dataClasses.homeScreen.ScheduleToCreate
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class AddScheduleViewModel: ViewModel() {

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
    var devicesToAdd by mutableStateOf(emptyList<GeneralDevice>())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var responseCode by mutableStateOf<Response<Void>?>(null)
        private set

    fun getAllDevicesFromHub(){
        viewModelScope.launch {
            devicesToAdd = getAllDevicesFromHubAsync()
        }
    }

    suspend fun getAllDevicesFromHubAsync(): List<GeneralDevice> {
        return try {
            isLoading = true
            val response = RetrofitClient.hubService.getAllDevices("1")
            // verify if it has schedules
            isLoading = false
            response.devices
        } catch (e: Exception) {
            // Handle network errors
            Log.e("API Request", "Error: ${e.message}", e)
            emptyList()
        }
    }
    fun addDeviceToSchedule(newDevice: Device) {
        val updatedSchedule = schedule.copy(devices = schedule.devices + newDevice)
        schedule = updatedSchedule
    }
    fun deleteDevice(deviceId: String) {
        val updatedDevices = schedule.devices.filter { it.macAddress != deviceId }
        schedule = schedule.copy(devices = updatedDevices)
    }

    fun createSchedule(
        scheduleName: String,
        from: String,
        until: String,
        days: List<Int>,
        callback: (Response<Void>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.scheduleService.createSchedule(
                    "1",
                    ScheduleToCreate(
                        schedule.devices,
                        schedule.isActive,
                        scheduleName,
                        from,
                        until,
                        days
                    )
                )
                Log.e("CREATE SCHEDULE ERROR", response.toString())
                responseCode = response
                callback(response) // Invoke the callback with the response
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("API Request", "Error: ${e.message}", e)
                callback(Response.error(500, ResponseBody.create(null, "Internal Server Error"))) // Return error response
            }
        }
    }
    
}