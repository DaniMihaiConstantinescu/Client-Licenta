package com.example.testapp.utils.api

import com.example.testapp.utils.dataClasses.homeScreen.Scene
import com.example.testapp.utils.dataClasses.homeScreen.Schedule

data class AllScenesResponse(
    val message: String,
    val scenes: List<Scene>
)
data class SceneResponse(
    val message: String,
    val scenes: Map<String, Scene>
)

data class ScheduleResponse(
    val message: String,
    val schedules: Map<String, Schedule>
)
data class AllSchedulesResponse(
    val message: String,
    val schedules: List<Schedule>
)
