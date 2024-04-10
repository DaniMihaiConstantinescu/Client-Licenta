package com.example.testapp.utils.viewModels.rooms

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.dataClasses.roomsScreen.Room
import com.example.testapp.utils.dataClasses.roomsScreen.RoomToCreate
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class AllRoomsViewModel: ViewModel() {

    var allRooms by mutableStateOf<List<Room>>(emptyList())
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
                val response = RetrofitClient.roomService.getAllRooms(userId)
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

    fun createRoom(roomName: String, callback: (Response<Void>) -> Unit){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.roomService.createRoom(
                    userId,
                    RoomToCreate(
                        roomName = roomName,
                        devices = emptyList()
                    )
                )
                callback(response)
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("API Request", "Error: ${e.message}", e)
                callback(Response.error(500, ResponseBody.create(null, "Internal Server Error"))) // Return error response
            }
        }
    }

    fun refreshRooms(){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.roomService.getAllRooms(userId)
                // verify if it has rooms
                val rooms = response.rooms
                allRooms = rooms ?: emptyList()
            } catch (e: Exception) {
                // Handle network errors
                Log.e("API Request", "Error: ${e.message}", e)
            }
        }
    }

    fun deleteRoom(roomId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.roomService.deleteRoom(userId, roomId)
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