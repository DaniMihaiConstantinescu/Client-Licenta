package com.example.testapp.ui.homepage

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.testapp.ui.homepage.screens.HomeScreen
import com.example.testapp.ui.homepage.screens.RoomsScreen
import com.example.testapp.ui.homepage.screens.SettingsScreen

@Composable
fun BottomNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = "home"
    ){
        composable(route = "home"){
            HomeScreen()
        }
        composable(route = "rooms"){
            RoomsScreen()
        }
        composable(route = "settings"){
            SettingsScreen()
        }
    }

}