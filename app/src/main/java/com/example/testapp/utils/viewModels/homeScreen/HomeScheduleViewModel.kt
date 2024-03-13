package com.example.testapp.utils.viewModels.homeScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.testapp.utils.dataClasses.homeScreen.Scene
import com.example.testapp.utils.dataClasses.general.Device
import com.example.testapp.utils.dataClasses.general.Settings
import com.example.testapp.utils.dataClasses.homeScreen.Schedule

class HomeScheduleViewModel: ViewModel(){

    private val testSchedules = listOf(
        Schedule(
            days = listOf(0, 1, 2, 3, 4, 5, 6),
            devices = listOf(
                Device(
                    macAddress = "addressamac1",
                    settings = Settings(
                        listOf(
                            Pair("temperature", "22.5")
                        )
                    )
                )
            ),
            from = "15:30",
            isActive = true,
            scheduleId = "-NsoLNaH3j3bOKQeqClR1",
            scheduleName = "Schedule 1",
            until = "16:00"
        ),
        Schedule(
            days = listOf(0,1, 2, 3, 6),
            devices = listOf(
                Device(
                    macAddress = "addressamac2",
                    settings = Settings(
                        listOf(
                            Pair("temperature", "22.5")
                        )
                    )
                )
            ),
            from = "16:30",
            isActive = false,
            scheduleId = "-NsoLNaH3j3bOKQeqClR2",
            scheduleName = "Schedule 2",
            until = "17:00"
        )
    )


    var topSchedules by mutableStateOf<List<Schedule>>(testSchedules)
        private set

    fun toggleSchedule(userId: String, scheduleId: String){
        // call API
        return
    }

}



