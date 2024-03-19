package com.example.testapp.utils.api.apiServices

import android.util.Log
import com.example.testapp.utils.api.AllScenesResponse
import com.example.testapp.utils.api.RetrofitClient
import com.example.testapp.utils.api.SceneResponse
import com.example.testapp.utils.dataClasses.general.Device
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val mainResource ="scenes"
interface SceneApiService {

    @GET("/$mainResource/{userId}")
    suspend fun getAllScenes(@Path("userId") userId: String): AllScenesResponse
    @GET("/$mainResource/top3/{userId}")
    suspend fun getTopScenes(@Path("userId") userId: String): AllScenesResponse
    @GET("/$mainResource/{userId}/{sceneId}")
    suspend fun getScene(@Path("userId") userId: String, @Path("sceneId") sceneId: String): SceneResponse
    @POST("/$mainResource/{userId}/{sceneId}/add-device")
    suspend fun addDeviceToScene(
        @Path("userId") userId: String,
        @Path("sceneId") sceneId: String,
        @Body newDevice: Device
    ): Response<Void>
    @DELETE("/$mainResource/{userId}/{sceneId}/{macAddress}")
    suspend fun deleteDeviceFromScene(
        @Path("userId") userId: String,
        @Path("sceneId") sceneId: String,
        @Path ("macAddress") macAddress: String
    ): Response<Void>

}
