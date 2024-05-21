package com.example.testapp.ui.homepage.home.hubs

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapp.utils.dataClasses.general.GeneralDevice
import com.example.testapp.utils.dataClasses.hubScreen.Hub
import com.example.testapp.utils.funcs.RenderDeviceIcon
import com.example.testapp.utils.viewModels.hubs.AllHubsViewModel
import com.example.testapp.utils.viewModels.hubs.HubDevicesViewModel

@Composable
fun AllHubsScreen(){
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
            Text(text = "Hubs", style = MaterialTheme.typography.headlineMedium)
        }

        HubsList()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubsList(){

    val hubViewModel = viewModel<AllHubsViewModel>()
    var selectedHub by remember { mutableStateOf<Hub?>(null) }
    
    if (selectedHub != null ){
        DevicesDialog(
            closeDialog = { selectedHub = null },
            devices = selectedHub!!.devices
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, top = 10.dp)
    ) {
    if (hubViewModel.isLoading){
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
        if (hubViewModel.allHubs.isEmpty()){
            item{
                Text(
                    text = "There is no hub added yet!",
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
            items(hubViewModel.allHubs) { hub ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    onClick = { selectedHub = hub }
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = hub.macAddress,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Text(text = "Devices connected: ${hub.devices.size}")
                    }
                }
            }
        }
    }

}

@Composable
fun DevicesDialog(
    closeDialog: () -> Unit,
    devices: List<String>
){
    val hubDevicesViewModel = viewModel<HubDevicesViewModel>()
    hubDevicesViewModel.getDevices(devices)

    DevicesDialog(
        onConfirmation = { closeDialog() },
        dialogTitle = "All devices",
        dialogText = hubDevicesViewModel.allDevices
    )
}


@Composable
fun DevicesDialog(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: List<GeneralDevice>,
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
               LazyColumn{
                   items(dialogText){device ->
                       Card(
                           colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                       ) {
                           Row(
                               Modifier
                                   .fillMaxWidth()
                                   .padding(vertical = 10.dp, horizontal = 6.dp),
                               horizontalArrangement = Arrangement.SpaceBetween
                           ) {
                               Text(text = device.name)
                               RenderDeviceIcon(deviceType = device.type)
                           }
                       }
                       Spacer(modifier = Modifier.height(8.dp))
                   }
               }
        },
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                },
                Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
}