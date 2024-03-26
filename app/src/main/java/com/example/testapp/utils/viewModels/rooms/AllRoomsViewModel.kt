package com.example.testapp.utils.viewModels.rooms

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.dataClasses.homeScreen.Room
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AllRoomsViewModel: ViewModel() {

    var allRooms by mutableStateOf<List<Room>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = RetrofitClient.roomservice.getAllRooms("1")
                // verify if it has rooms
                val rooms = response.rooms
                isLoading = false
                allRooms = rooms ?: emptyList()
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
                isLoading = false
            }
        }
    }

    fun deleteRoom(roomId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.roomservice.deleteRoom("1", roomId)
                if (response.isSuccessful) {
                    allRooms = allRooms.filter { it.roomId != roomId }
                } else {
                    Log.e("DELETE ROOM ERROR", "HTTP Error: ${response.code()}")
                }
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    Log.e("DELETE ROOM ERROR", "Room not found")
                } else {
                    Log.e("DELETE ROOM ERROR", "HTTP Error")
                }
            } catch (e: Exception) {
                Log.e("DELETE ROOM ERROR", "Server Error")
            }
        }
    }
    
}