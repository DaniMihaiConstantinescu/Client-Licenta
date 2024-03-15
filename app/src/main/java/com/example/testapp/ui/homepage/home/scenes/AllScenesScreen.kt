package com.example.testapp.ui.homepage.home.scenes

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.testapp.ui.homepage.home.SceneCard
import com.example.testapp.ui.homepage.home.common.AddButtonRow
import com.example.testapp.utils.viewModels.homeScreen.Scenes.AllScenesViewModel

@Composable
fun AllScenesScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Scenes", style = MaterialTheme.typography.headlineMedium)
        }
        AddButtonRow(onClick = { }, text = "Add Scene")
        ScenesRow(navController = navController)
    }

}

@Composable
fun ScenesRow(navController: NavController){

    val sceneViewModel = viewModel<AllScenesViewModel>()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, top = 10.dp)
    ) {
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
            if (sceneViewModel.allScenes.isEmpty()){
                item{
                    Text(
                        text = "There is no scene created yet!",
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
                items(sceneViewModel.allScenes) { scene ->
                    SceneCard(scene, navController = navController)
                }
            }
    }

}