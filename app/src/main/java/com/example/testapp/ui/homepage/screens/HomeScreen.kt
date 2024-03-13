package com.example.testapp.ui.homepage.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapp.ui.homepage.home.SceneCard
import com.example.testapp.ui.homepage.home.ScheduleCard
import com.example.testapp.utils.viewModels.homeScreen.HomeSceneViewModel
import com.example.testapp.utils.viewModels.homeScreen.HomeScheduleViewModel

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp),
    ) {
        Column {
            ScenePart()
            SchedulePart()
        }
    }
}

@Composable
fun ScenePart() {

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
                    onClick = { /*TODO*/ },
                ) {
                    Text(text = "All Scenes >", style = MaterialTheme.typography.bodyLarge)
                }

            }
        }
        items(sceneViewModel.topScenes) { scene ->
            SceneCard(scene)
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
        items(scheduleViewModel.topSchedules) { schedule ->
            ScheduleCard(schedule)
        }

    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen()
}