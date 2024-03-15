package com.example.testapp.utils.api.apiServices

import com.example.testapp.utils.api.DeviceResponse
import com.example.testapp.utils.api.DevicesResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val mainResource ="devices"
interface DeviceApiService {
    @POST("/$mainResource/not-in")
    suspend fun getDevicesNotInList(@Body devices: List<String>): DevicesResponse
    @GET("/$mainResource/{deviceId}")
    suspend fun getDevice(@Path("userId") userId: String, @Path("deviceId") sceneId: String): DeviceResponse

}
