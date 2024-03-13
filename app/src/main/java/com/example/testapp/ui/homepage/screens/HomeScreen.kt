package com.example.testapp.ui.homepage.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testapp.ui.homepage.home.SceneCard
import com.example.testapp.ui.homepage.home.ScheduleCard

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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        // Header
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
        // Map over the scenes
        SceneCard(
            text = "Winter Scene",
        )
        SceneCard(
            text = "Summer Scene",
        )
    }
}

@Composable
fun SchedulePart() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        // Header
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
        // Map over the schedules
        ScheduleCard(
            title = "Water the plants!",
            interval = "7:30 - 08:00",
            repetition = "Every day",
        )
        ScheduleCard(
            title = "Turn on the humidifier",
            interval = "7:30 - 08:00",
            repetition = "Every monday",
        )
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen()
}