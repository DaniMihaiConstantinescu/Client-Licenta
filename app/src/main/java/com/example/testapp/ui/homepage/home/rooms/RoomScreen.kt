package com.example.testapp.ui.homepage.home.rooms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapp.ui.homepage.home.common.AddButtonRow
import com.example.testapp.utils.dataClasses.general.GeneralDevice
import com.example.testapp.utils.enums.IconsIds
import com.example.testapp.utils.viewModels.rooms.RoomViewModel

@Composable
fun RoomScreen(roomId: String){

    val roomViewModel = viewModel<RoomViewModel>(
        factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RoomViewModel(
                    roomId
                ) as T
            }
        }
    )
    val room = roomViewModel.room

    var showAddDialog1 by remember { mutableStateOf(false) }

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
            Text(text = room.roomName, style = MaterialTheme.typography.headlineMedium)
        }

        AddButtonRow(onClick = { showAddDialog1 = true }, text = "Add Device")
        DeviceColumn(roomViewModel)

        if (showAddDialog1) {
            AddDialogPage01(
                viewModel = roomViewModel,
                onDismissRequest = { showAddDialog1 = false },
                onConfirmation = { addDevice ->
                    showAddDialog1 = false
                    roomViewModel.addDeviceToRoom(addDevice)
                }
            )
        }
        
    }
}

@Composable
fun DeviceColumn(
    viewModel: RoomViewModel
) {

    var devices by remember { mutableStateOf(emptyList<GeneralDevice>()) }
    viewModel.getDevicesDetails { devicesWithDetails ->
        devices = devicesWithDetails
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, top = 10.dp)
    ) {

        if (devices.isEmpty()){
            item{
                Text(
                    text = "There is no device added yet!",
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
            items(devices) { device ->
                if (device != null )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick =
                            {
                                viewModel.deleteDevice(device.deviceMAC)
                            },
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(16)
                                )
                                .size(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete device",
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Card(
                            modifier = Modifier
                                .clickable { }
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = device.name, color = Color.White , style = MaterialTheme.typography.titleLarge)
                                RenderDeviceIcon(deviceType = device.type)
                            }
                        }

                    }
            }
        }
    }

}
@Composable
fun RenderDeviceIcon(deviceType: String){

    when (deviceType){
        "ac" -> Icon(
            painter = painterResource(IconsIds.AC.drawableResId),
            contentDescription = IconsIds.AC.iconName
        )
        "dehumifier" -> Icon(
            painter = painterResource(IconsIds.DEHUIDIFIER.drawableResId),
            contentDescription = IconsIds.DEHUIDIFIER.iconName
        )
        "light" -> Icon(
            painter = painterResource(IconsIds.LIGHT.drawableResId),
            contentDescription = IconsIds.DEHUIDIFIER.iconName
        )
        "shutter" -> Icon(
            painter = painterResource(IconsIds.SHUTTER.drawableResId),
            contentDescription = IconsIds.SHUTTER.iconName
        )
    }

}


@Composable
fun AddDialogPage01(
    viewModel: RoomViewModel,
    onDismissRequest: () -> Unit,
    onConfirmation: (device: GeneralDevice) -> Unit,
) {

    var devicesToAdd by remember { mutableStateOf(emptyList<GeneralDevice>()) }
    viewModel.getDevicesNotIn{ devices ->
        devicesToAdd = devices
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(560.dp)
                .padding(10.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column {
                    Text(
                        text = "Select Device",
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ){
                        items(devicesToAdd){device ->
                            Card(
                                modifier = Modifier
                                    .clickable { onConfirmation(device) }
                                    .padding(bottom = 8.dp)
                            ) {
                                Text(
                                    text = device.name,
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.surface)
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        }
    }
}