package com.example.testapp.ui.homepage.mainScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.testapp.ui.homepage.home.scenes.SceneCard
import com.example.testapp.ui.homepage.home.schedules.ScheduleCard
import com.example.testapp.utils.auth.logic.UserData
import com.example.testapp.utils.viewModels.scenes.HomeSceneViewModel
import com.example.testapp.utils.viewModels.homeScreen.Schedules.HomeScheduleViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    userData: UserData?
) {
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 46.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    if (userData?.userName != null) {
                        Text(
                            text = "Welcome, " + userData.userName,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 26.sp,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 26.sp
                        )
                    }
                }
                if (userData?.profilePicURL != null) {
                    AsyncImage(
                        model = userData.profilePicURL,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(48.dp) // Adjust the size of the image as needed
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            ScenePart(navController)
            SchedulePart(navController)
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
            if (sceneViewModel.topScenes.isEmpty()){
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
                items(sceneViewModel.topScenes) { scene ->
                    SceneCard(scene, navController)
                    Spacer(modifier = Modifier.height(8.dp) )
                }
            }
    }
}

@Composable
fun SchedulePart(navController: NavController) {

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
                    onClick = { navController.navigate("allSchedules") },
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
            if (scheduleViewModel.topSchedules.isEmpty()){
                item{
                    Text(
                        text = "There is no schedule created yet!",
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
                    ScheduleCard(schedule, navController)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
    }
}