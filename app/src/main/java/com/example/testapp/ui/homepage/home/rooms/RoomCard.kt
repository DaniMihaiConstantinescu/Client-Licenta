package com.example.testapp.ui.homepage.home.rooms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testapp.utils.dataClasses.roomsScreen.Room

@Composable
fun RoomCard(
    room: Room,
    navController: NavController
){

    Card(
        modifier = Modifier
            .clickable { navController.navigate("room/${room.roomId}") }
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = room.roomName, color = Color.White , style = MaterialTheme.typography.bodyLarge)
            Text(
                text = if (room.devices.isEmpty()) "No Device Connected" else "${room.devices.size} Devices Connected"
            )
        }
    }

}