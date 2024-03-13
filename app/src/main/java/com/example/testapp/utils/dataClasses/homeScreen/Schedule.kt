package com.example.testapp.utils.dataClasses.homeScreen

import com.example.testapp.utils.dataClasses.general.Device

data class ScheduleResponse(
    val message: String,
    val schedules: List<Schedule>
)

data class Schedule(
    val days: List<Int>,
    val devices: List<Device>,
    val from: String,
    val isActive: Boolean,
    val scheduleId: String,
    val scheduleName: String,
    val until: String
)
