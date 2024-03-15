package com.example.testapp.ui.homepage.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testapp.utils.dataClasses.homeScreen.Scene

@Composable
fun SceneCard(scene: Scene, navController: NavController) {
    var checked by remember { mutableStateOf(scene.isActive) }

    Card(
        modifier = Modifier
            .clickable{ navController.navigate("scene/${scene.sceneId}") }
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = scene.sceneName, color = Color.White , style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                }
            )
        }
    }
}
