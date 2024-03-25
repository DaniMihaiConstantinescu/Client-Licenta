package com.example.testapp.ui.homepage.home

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.testapp.utils.dataClasses.homeScreen.Scene
import com.example.testapp.utils.viewModels.homeScreen.Scenes.SceneCardViewModel
import kotlinx.coroutines.coroutineScope

@Composable
fun SceneCard(
    scene: Scene,
    navController: NavController
) {
    var checked by remember { mutableStateOf(scene.isActive) }
    val sceneViewModel = viewModel<SceneCardViewModel>()
    var context = LocalContext.current

    Card(
        modifier = Modifier
            .clickable { navController.navigate("scene/${scene.sceneId}") }
            .fillMaxWidth()
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
                    sceneViewModel.toggleScene(scene.sceneId) { success ->
                        if (success) {
                            checked = it
                        } else {
                            Toast.makeText(context, "An error occurred while toggling the scene ", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }

}
