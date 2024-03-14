package com.example.testapp.ui.homepage.mainScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.testapp.ui.homepage.home.SceneCard
import com.example.testapp.ui.homepage.home.ScheduleCard
import com.example.testapp.utils.viewModels.homeScreen.HomeSceneViewModel
import com.example.testapp.utils.viewModels.homeScreen.HomeScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp),

            ) {
            Column {
                ScenePart(navController)
                SchedulePart()
            }
        }
    }

}

@Composable
fun ScenePart(navController: NavController) {

    val sceneViewModel = viewModel<HomeSceneViewModel>()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Scenes", style = MaterialTheme.typography.headlineSmall)
                TextButton(
                    onClick = { navController.navigate("allScenes") },
                ) {
                    Text(text = "All Scenes >", style = MaterialTheme.typography.bodyLarge)
                }

            }
        }

        if (sceneViewModel.isLoading){
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }else
            if (sceneViewModel.topScenes.size == 0){
                item{
                    Text(
                        text = "There is no scene created!",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(vertical = 16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }else {
                items(sceneViewModel.topScenes) { scene ->
                    SceneCard(scene)
                }
            }
    }
}

@Composable
fun SchedulePart() {

    val scheduleViewModel = viewModel<HomeScheduleViewModel>()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Schedule", style = MaterialTheme.typography.headlineSmall)
                TextButton(
                    onClick = { /*TODO*/ },
                ) {
                    Text(text = "All Schedules >", style = MaterialTheme.typography.bodyLarge)
                }

            }
        }

        if (scheduleViewModel.isLoading){
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }else
            if (scheduleViewModel.topSchedules.size == 0){
                item{
                    Text(
                        text = "There is no schedule created!",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(vertical = 16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }else {
                items(scheduleViewModel.topSchedules) { schedule ->
                    ScheduleCard(schedule)
                }
            }
    }
}