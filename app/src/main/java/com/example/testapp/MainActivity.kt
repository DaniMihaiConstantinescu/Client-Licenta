package com.example.testapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.testapp.ui.theme.TestAppTheme
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.testapp.ui.auth.SignInScreen
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
import com.example.testapp.utils.auth.logic.GoogleUiClient
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold {

                        val navController = rememberNavController()

                        NavHost(
                            navController = navController,
                            startDestination = "sign_in"
                        ){
                            composable("sign_in") {
                                val viewModel = viewModel<SignInViewModel>()
                                val state by viewModel.state.collectAsStateWithLifecycle()

                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult = {result ->
                                        if (result.resultCode == RESULT_OK) {
                                            lifecycleScope.launch {
                                                val signInResult = googleAuthUiClient.signInWithIntent(
                                                    intent = result.data ?: return@launch
                                                )
                                                viewModel.onSingInResult(signInResult)
                                            }
                                        }
                                    }
                                )
                                
                                LaunchedEffect(key1 = state.isSignInSuccessful) {
                                    if (state.isSignInSuccessful) {
                                        navController.navigate("home")
                                        viewModel.resetState()
                                    }
                                }
                                
                                SignInScreen(
                                    state = state,
                                    onSignInClick = {
                                        lifecycleScope.launch {
                                            val signInIntentSender = googleAuthUiClient.signIn()
                                            launcher.launch(
                                                IntentSenderRequest.Builder(
                                                    signInIntentSender ?: return@launch
                                                ).build()
                                            )
                                        }
                                    }
                                )
                            }
                            composable(route = "home") {
                                HomeScreen(
                                    navController,
                                    userData = googleAuthUiClient.getSignedInUser(),
                                    onSignOut = {
                                        lifecycleScope.launch {
                                            googleAuthUiClient.signOut()
                                            Toast.makeText(applicationContext, "Signed Out", Toast.LENGTH_SHORT).show()

                                            navController.navigate("sign_in")
                                        }
                                    }
                                )
                            }
                            composable(route = "rooms") {
                                RoomsScreen(navController)
                            }
                            composable(route = "room/{roomId}") { backStackEntry ->
                                val roomId = backStackEntry.arguments?.getString("roomId")
                                roomId?.let { id ->
                                    // Pass the room ID to the RoomScreen
                                    RoomScreen(roomId = id)
                                }
                            }
                            composable(route = "settings") {
                                SettingsScreen(navController)
                            }
                            composable(route = "allScenes") {
                                AllScenesScreen(navController)
                            }
                            composable(route = "addScene") {
                                AddSceneScreen(navController)
                            }
                            composable(route = "scene/{sceneId}") { backStackEntry ->
                                val sceneId = backStackEntry.arguments?.getString("sceneId")
                                sceneId?.let { id ->
                                    // Pass the scene ID to the SceneScreen
                                    SceneScreen(sceneId = id)
                                }
                            }
                            composable(route = "allSchedules") {
                                AllSchedulesScreen(navController)
                            }
                            composable(route = "addSchedule") {
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
                }

            }
        }
    }
}
