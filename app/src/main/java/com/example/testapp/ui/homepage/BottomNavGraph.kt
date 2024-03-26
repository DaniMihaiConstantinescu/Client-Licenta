package com.example.testapp.ui.homepage

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.testapp.ui.homepage.home.rooms.RoomScreen
import com.example.testapp.ui.homepage.home.scenes.AddSceneScreen
import com.example.testapp.ui.homepage.mainScreens.HomeScreen
import com.example.testapp.ui.homepage.mainScreens.RoomsScreen
import com.example.testapp.ui.homepage.mainScreens.SettingsScreen
import com.example.testapp.ui.homepage.home.scenes.AllScenesScreen
import com.example.testapp.ui.homepage.home.scenes.SceneScreen
import com.example.testapp.ui.homepage.home.schedules.AddScheduleScreen
import com.example.testapp.ui.homepage.home.schedules.AllSchedulesScreen
import com.example.testapp.ui.homepage.home.schedules.ScheduleScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavGraph(navController: NavController){
    NavHost(
        navController = navController as NavHostController,
        startDestination = "home"
    ){
        composable(route = "home"){
            HomeScreen(navController)
        }
        composable(route = "rooms"){
            RoomsScreen(navController)
        }
        composable(route = "room/{roomId}") { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId")
            roomId?.let { id ->
                // Pass the room ID to the RoomScreen
                RoomScreen(roomId = id)
            }
        }
        composable(route = "settings"){
            SettingsScreen(navController)
        }
        composable(route = "allScenes"){
            AllScenesScreen(navController)
        }
        composable(route = "addScene"){
            AddSceneScreen(navController)
        }
        composable(route = "scene/{sceneId}") { backStackEntry ->
            val sceneId = backStackEntry.arguments?.getString("sceneId")
            sceneId?.let { id ->
                // Pass the scene ID to the SceneScreen
                SceneScreen(sceneId = id)
            }
        }
        composable(route = "allSchedules"){
            AllSchedulesScreen(navController)
        }
        composable(route = "addSchedule"){
            AddScheduleScreen(navController)
        }
        composable(route = "schedule/{scheduleId}") { backStackEntry ->
            val scheduleId = backStackEntry.arguments?.getString("scheduleId")
            scheduleId?.let { id ->
                // Pass the scene ID to the SceneScreen
                ScheduleScreen(scheduleId = id)
            }
        }
    }

}