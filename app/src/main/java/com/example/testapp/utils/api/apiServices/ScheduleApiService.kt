package com.example.testapp.utils.api.apiServices

import android.util.Log
import com.example.testapp.utils.api.AllScenesResponse
import com.example.testapp.utils.api.AllSchedulesResponse
import com.example.testapp.utils.api.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

private const val mainResource ="schedules"
interface ScheduleApiService {

    @GET("/$mainResource/{userId}")
    suspend fun getAllSchedules(@Path("userId") userId: String): AllSchedulesResponse

    @GET("/$mainResource/top3/{userId}")
    suspend fun getTopSchedules(@Path("userId") userId: String): AllSchedulesResponse

}