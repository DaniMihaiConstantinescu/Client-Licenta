package com.example.testapp.utils.api.apiServices

import com.example.testapp.utils.api.DevicesResponse
import retrofit2.http.GET
import retrofit2.http.Path

private const val mainResource ="hubs"
interface HubApiService {
    @GET("/$mainResource/{userId}/all-devices")
    suspend fun getAllDevices(
        @Path("userId") userId: String
    ): DevicesResponse

}