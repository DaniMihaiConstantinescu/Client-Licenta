package com.example.testapp.utils.dataClasses.homeScreen

import com.example.testapp.utils.dataClasses.general.Device

data class ScheduleToCreate(
    val devices: List<Device>,
    val isActive: Boolean,
    val scheduleName: String,
    val from: String,
    val until: String,
    val days: List<Int>
)
