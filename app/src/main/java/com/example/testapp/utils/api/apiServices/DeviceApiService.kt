package com.example.testapp.utils.api.apiServices

import com.example.testapp.utils.api.DeviceResponse
import com.example.testapp.utils.api.DevicesResponse
import com.example.testapp.utils.dataClasses.general.Device
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val mainResource ="devices"
interface DeviceApiService {
    @POST("/$mainResource/not-in")
    suspend fun getDevicesNotInList(@Body devices: List<String>): DevicesResponse
    @POST("/$mainResource/in-list")
    suspend fun getDevicesInList(@Body devices: List<String>): DevicesResponse
    @GET("/$mainResource/{userId}/{deviceId}")
    suspend fun getDevice(@Path("userId") userId: String, @Path("deviceId") deviceId: String): DeviceResponse
}
