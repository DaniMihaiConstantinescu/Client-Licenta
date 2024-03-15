package com.example.testapp.ui.homepage.home.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.testapp.utils.dataClasses.general.Device
import com.example.testapp.utils.viewModels.homeScreen.Scenes.SceneViewModel

@Composable
fun DeviceWithSettingsCard(device: Device, sceneViewModel: SceneViewModel){

    Card(
        modifier = Modifier
            .clickable {  }
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = device.macAddress, color = Color.White , style = MaterialTheme.typography.titleLarge)
            Text(
                text = device.settings.values.first(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }

}