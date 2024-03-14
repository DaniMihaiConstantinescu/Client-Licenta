package com.example.testapp.utils.api

import com.example.testapp.utils.api.apiServices.SceneApiService
import com.example.testapp.utils.api.apiServices.ScheduleApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    private const val currentIp = "192.168.1.102"
    private const val baseUrl = "http://$currentIp:5000"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val sceneService: SceneApiService by lazy {
        retrofit.create(SceneApiService::class.java)
    }
    val scheduleService: ScheduleApiService by lazy {
        retrofit.create(ScheduleApiService::class.java)
    }
}