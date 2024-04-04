package com.example.testapp.ui.homepage

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testapp.ui.homepage.home.rooms.RoomScreen
import com.example.testapp.ui.homepage.home.scenes.AddSceneScreen
import com.example.testapp.ui.homepage.home.scenes.AllScenesScreen
import com.example.testapp.ui.homepage.home.scenes.SceneScreen
import com.example.testapp.ui.homepage.home.schedules.AddScheduleScreen
import com.example.testapp.ui.homepage.home.schedules.AllSchedulesScreen
import com.example.testapp.ui.homepage.home.schedules.ScheduleScreen
import com.example.testapp.ui.homepage.mainScreens.HomeScreen
import com.example.testapp.ui.homepage.mainScreens.RoomsScreen
import com.example.testapp.ui.homepage.mainScreens.SettingsScreen
import com.example.testapp.utils.auth.SignInViewModel

//@RequiresApi(Build.VERSION_CODES.O)
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun MainScreen() {
//
//    val navController = rememberNavController()
//
//    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//        Scaffold {
//            NavHost(
//                navController = navController,
//                startDestination = "sign_in"
//            ){
//                composable("sign_in") {
//                    val viewModel = viewModel<SignInViewModel>()
//                    val state by viewModel.state.collectAsStateWithLifecycle()
//
//                    val launcher = rememberLauncherForActivityResult(
//                        contract = ActivityResultContracts.StartActivityForResult(),
//                        onResult = {result ->
//
//                        }
//                    )
//                }
//                composable(route = "home") {
//                    HomeScreen(navController)
//                }
//                composable(route = "rooms") {
//                    RoomsScreen(navController)
//                }
//                composable(route = "room/{roomId}") { backStackEntry ->
//                    val roomId = backStackEntry.arguments?.getString("roomId")
//                    roomId?.let { id ->
//                        // Pass the room ID to the RoomScreen
//                        RoomScreen(roomId = id)
//                    }
//                }
//                composable(route = "settings") {
//                    SettingsScreen(navController)
//                }
//                composable(route = "allScenes") {
//                    AllScenesScreen(navController)
//                }
//                composable(route = "addScene") {
//                    AddSceneScreen(navController)
//                }
//                composable(route = "scene/{sceneId}") { backStackEntry ->
//                    val sceneId = backStackEntry.arguments?.getString("sceneId")
//                    sceneId?.let { id ->
//                        // Pass the scene ID to the SceneScreen
//                        SceneScreen(sceneId = id)
//                    }
//                }
//                composable(route = "allSchedules") {
//                    AllSchedulesScreen(navController)
//                }
//                composable(route = "addSchedule") {
//                    AddScheduleScreen(navController)
//                }
//                composable(route = "schedule/{scheduleId}") { backStackEntry ->
//                    val scheduleId = backStackEntry.arguments?.getString("scheduleId")
//                    scheduleId?.let { id ->
//                        // Pass the scene ID to the SceneScreen
//                        ScheduleScreen(scheduleId = id)
//                    }
//                }
//            }
//        }
//    }
//}