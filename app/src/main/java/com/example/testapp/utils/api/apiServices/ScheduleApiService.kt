package com.example.testapp.utils.api.apiServices

import com.example.testapp.utils.api.AllSchedulesResponse
import com.example.testapp.utils.api.SceneResponse
import com.example.testapp.utils.api.ScheduleResponse
import com.example.testapp.utils.dataClasses.general.Device
import com.example.testapp.utils.dataClasses.homeScreen.SceneToCreate
import com.example.testapp.utils.dataClasses.homeScreen.ScheduleToCreate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val mainResource ="schedules"
interface ScheduleApiService {

    @GET("/$mainResource/{userId}")
    suspend fun getAllSchedules(@Path("userId") userId: String): AllSchedulesResponse
    @GET("/$mainResource/top3/{userId}")
    suspend fun getTopSchedules(@Path("userId") userId: String): AllSchedulesResponse

    @GET("/$mainResource/{userId}/{scheduleId}")
    suspend fun getSchedule(@Path("userId") userId: String, @Path("scheduleId") scheduleId: String): ScheduleResponse
    @POST("/$mainResource/{userId}")
    suspend fun createSchedule(
        @Path("userId") userId: String,
        @Body schedule: ScheduleToCreate
    ): Response<Void>
    @POST("/$mainResource/toggle/{userId}/{scheduleId}")
    suspend fun toggleSchedule(
        @Path("userId") userId: String,
        @Path("scheduleId") scheduleId: String,
    ): Response<Void>
    @DELETE("/$mainResource/{userId}/{scheduleId}")
    suspend fun deleteSchedule(
        @Path("userId") userId: String,
        @Path("scheduleId") scheduleId: String,
    ): Response<Void>


    @POST("/$mainResource/add-device/{userId}/{scheduleId}")
    suspend fun addDeviceToSchedule(
        @Path("userId") userId: String,
        @Path("scheduleId") scheduleId: String,
        @Body newDevice: Device
    ): Response<Void>
    @DELETE("/$mainResource/remove-device/{userId}/{scheduleId}/{macAddress}")
    suspend fun deleteDeviceFromSchedule(
        @Path("userId") userId: String,
        @Path("scheduleId") scheduleId: String,
        @Path ("macAddress") macAddress: String
    ): Response<Void>
}