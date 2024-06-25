package com.example.testapp.utils.api.apiServices

import com.example.testapp.utils.api.AllRoomsResponse
import com.example.testapp.utils.api.RoomResponse
import com.example.testapp.utils.dataClasses.general.Device
import com.example.testapp.utils.dataClasses.general.GeneralDevice
import com.example.testapp.utils.dataClasses.roomsScreen.RoomToCreate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val mainResource ="rooms"
interface RoomApiService {
    @GET("/$mainResource/{userId}")
    suspend fun getAllRooms(@Path("userId") userId: String): AllRoomsResponse


    @GET("/$mainResource/{userId}/{roomId}")
    suspend fun getRoom(
        @Path("userId") userId: String,
        @Path("roomId") roomId: String
    ): RoomResponse
    @POST("/$mainResource/{userId}")
    suspend fun createRoom(
        @Path("userId") userId: String,
        @Body room: RoomToCreate
    ): Response<Void>
    @DELETE("/$mainResource/{userId}/{roomId}")
    suspend fun deleteRoom(
        @Path("userId") userId: String,
        @Path("roomId") roomId: String,
    ): Response<Void>


    @POST("/$mainResource/add-device/{userId}/{roomId}")
    suspend fun addDeviceToRoom(
        @Path("userId") userId: String,
        @Path("roomId") roomId: String,
        @Body newDevice: GeneralDevice
    ): Response<Void>
    @DELETE("/$mainResource/remove-device/{userId}/{roomId}/{macAddress}")
    suspend fun deleteDeviceFromRoom(
        @Path("userId") userId: String,
        @Path("roomId") roomId: String,
        @Path ("macAddress") macAddress: String
    ): Response<Void>

}