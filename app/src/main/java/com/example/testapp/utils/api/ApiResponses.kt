package com.example.testapp.utils.api

import com.example.testapp.utils.dataClasses.general.GeneralDevice
import com.example.testapp.utils.dataClasses.homeScreen.Scene
import com.example.testapp.utils.dataClasses.homeScreen.Schedule
import com.example.testapp.utils.dataClasses.roomsScreen.Room

data class AllScenesResponse(
    val message: String,
    val scenes: List<Scene>
)
data class SceneResponse(
    val message: String,
    val scene: Scene
)

data class ScheduleResponse(
    val message: String,
    val schedule: Schedule
)
data class AllSchedulesResponse(
    val message: String,
    val schedules: List<Schedule>
)

data class DeviceResponse(
    val message: String,
    val device: GeneralDevice
)
data class DevicesResponse(
    val message: String,
    val devices: List<GeneralDevice>
)

data class AllRoomsResponse(
    val message: String,
    val rooms: List<Room>
)
data class RoomResponse(
    val message: String,
    val room: Room
)