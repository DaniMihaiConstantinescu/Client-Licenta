package com.example.testapp.ui.homepage

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.testapp.ui.homepage.mainScreens.HomeScreen
import com.example.testapp.ui.homepage.mainScreens.RoomsScreen
import com.example.testapp.ui.homepage.mainScreens.SettingsScreen
import com.example.testapp.ui.homepage.home.scenes.AllScenesScreen

@Composable
fun BottomNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = "home"
    ){
        composable(route = "home"){
            HomeScreen(navController)
        }
        composable(route = "rooms"){
            RoomsScreen(navController)
        }
        composable(route = "settings"){
            SettingsScreen(navController)
        }
        composable(route = "allScenes"){
            AllScenesScreen()
        }
    }

}