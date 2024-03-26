package com.example.testapp.ui.homepage.mainScreens

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.testapp.ui.homepage.home.common.AddButtonRow
import com.example.testapp.ui.homepage.home.rooms.RoomCard
import com.example.testapp.utils.viewModels.rooms.AllRoomsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RoomsScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 20.dp),

                ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 35.dp, bottom = 20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Rooms", style = MaterialTheme.typography.headlineMedium)
                    }
                    RoomsColumn(navController = navController)
                }
            }
        }
    }
}

@Composable
fun RoomsColumn(navController: NavController){

    val roomsViewModel = viewModel<AllRoomsViewModel>()
    var showDialog by remember { mutableStateOf(false) }

    AddButtonRow(onClick = { showDialog = true }, text = "Add Room")
    Spacer(modifier = Modifier.height(4.dp))
    if (showDialog){
        AddRoomDialog(
            roomsViewModel,
            onCancel = { showDialog = false },
            onConfirm = {
                showDialog = false
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, top = 10.dp)
    ) {
        if (roomsViewModel.isLoading){
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
            if (roomsViewModel.allRooms.isEmpty()){
                item{
                    Text(
                        text = "There is no room created yet!",
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
                items(roomsViewModel.allRooms) { room ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                roomsViewModel.deleteRoom(room.roomId)
                            },
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(16)
                                )
                                .size(60.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete Scene",
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        RoomCard(room, navController)
                    }


                }
            }
    }
    
}

@Composable
fun AddRoomDialog(
    viewModel: AllRoomsViewModel,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
){

    var name by remember { mutableStateOf("") }
    val context = LocalContext.current

    Dialog(onDismissRequest = onCancel) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(420.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(top = 10.dp)
            ) {
                Text(text = "Room Name")
                TextField(
                    value = name,
                    onValueChange = {name = it},
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onCancel ) {
                        Text(text = "Cancel")
                    }
                    TextButton(onClick = {
                        if (name == "")
                            Toast.makeText(context, "Add a name to the room", Toast.LENGTH_SHORT).show()
                        else{

                            viewModel.createRoom(name){response ->
                                if (response.isSuccessful) {
                                    viewModel.refreshRooms()
                                    onConfirm()
                                } else {
                                    name = ""
                                    Toast.makeText(context, "Error while creating the room", Toast.LENGTH_SHORT).show()
                                    onCancel()
                                }
                            }
                        }
                    }) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }

}