package com.example.testapp.utils.viewModels.rooms

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.dataClasses.general.GeneralDevice
import com.example.testapp.utils.dataClasses.roomsScreen.Room
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RoomViewModel(
    private val roomId: String
): ViewModel() {
    var room by mutableStateOf(Room(devices = emptyList(), roomName = "", roomId))
        private set
    var devices by mutableStateOf(emptyList<GeneralDevice>())
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
                val response = RetrofitClient.roomService.getRoom(userId, roomId)
                // verify if it has rooms
                isLoading = false
                room = response.room ?: Room(devices = emptyList(), roomName = "", roomId)
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
                isLoading = false
            }

            try {
                isLoading = true
                val response = RetrofitClient.deviceService.getDevicesInList(room.devices)
                isLoading = false
                devices = response.devices ?: emptyList<GeneralDevice>()
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
                isLoading = false
            }
        }
    }

    fun getDevicesNotIn(callback: (List<GeneralDevice>) -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = RetrofitClient.hubService.getAllDevicesNotIn(userId, room.devices)
                isLoading = false
                val devicesList = response.devices ?: emptyList()
                callback(devicesList)
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
                isLoading = false
                callback(emptyList())
            }
        }
    }

    fun getDevicesDetails(callback: (List<GeneralDevice>) -> Unit){
        viewModelScope.launch {
            try {
                isLoading = true
                val response = RetrofitClient.deviceService.getDevicesInList(room.devices)
                isLoading = false
                val devicesList = response.devices ?: emptyList()
                callback(devicesList)
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
                isLoading = false
                callback(emptyList())
            }
        }
    }


    fun addDeviceToRoom(newDevice: GeneralDevice) {
        viewModelScope.launch {
            try {
                // Make the API call to add the device to the room
                val response = RetrofitClient.roomService.addDeviceToRoom(userId, roomId, newDevice)
                if (response.isSuccessful) {
                    val updatedRoom = room.copy(devices = room.devices + newDevice.deviceMAC)
                    room = updatedRoom
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
                val response = RetrofitClient.roomService.deleteDeviceFromRoom(userId, roomId, deviceId)
                if (response.isSuccessful) {
                    // Remove the device from the list
                    val updatedDevices = room.devices.filter { it != deviceId }
                    room = room.copy(devices = updatedDevices)
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