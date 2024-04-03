package com.example.testapp.ui.homepage.home.scenes

import android.widget.Toast
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapp.ui.homepage.home.common.AddButtonRow
import com.example.testapp.ui.homepage.home.common.DeviceWithSettingsCard
import com.example.testapp.ui.homepage.home.common.RenderDeviceSettings
import com.example.testapp.utils.dataClasses.general.Device
import com.example.testapp.utils.dataClasses.general.GeneralDevice
import com.example.testapp.utils.viewModels.scenes.SceneAddDeviceViewModel
import com.example.testapp.utils.viewModels.scenes.SceneViewModel

@Composable
fun SceneScreen(sceneId: String){

    val sceneViewModel = viewModel<SceneViewModel>(
        factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SceneViewModel(
                    sceneId = sceneId,
                ) as T
            }
        }
    )
    val scene = sceneViewModel.scene
    var text by rememberSaveable { mutableStateOf("") }

    var showAddDialog1 by remember { mutableStateOf(false) }
    var showAddDialog2 by remember { mutableStateOf(false) }
    var selectedAddDevice by remember { mutableStateOf(GeneralDevice(
        deviceMAC = "",
        hubMac = "",
        name = "",
        type = ""
    )) }

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
            Text(text = scene.sceneName, style = MaterialTheme.typography.headlineMedium)
        }
        TextField(
            modifier = Modifier
                .clickable { }
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            value = text,
            onValueChange = { text = it },
            label = { Text("Scene Name") },
            singleLine = true
        )
        AddButtonRow(onClick = { showAddDialog1 = true }, text = "Add Device")
        DeviceColumn(scene.devices, sceneViewModel)

        if (showAddDialog1) {
            AddDialogPage1(
                devicesInScene = scene.devices,
                onDismissRequest = { showAddDialog1 = false },
                onConfirmation = { addDevice ->
                    showAddDialog1 = false
                    showAddDialog2 = true
                    selectedAddDevice = addDevice
                }
            )
        }
        if (showAddDialog2){
            AddDialogPage2(
                onDismissRequest = { showAddDialog2 = false },
                onConfirmation = { settings ->
                    showAddDialog2 = false
                    sceneViewModel.addDeviceToScene(
                        newDevice = Device(
                            selectedAddDevice.deviceMAC,
                            settings,
                        )
                    )
                },
                selectedDevice = selectedAddDevice
            )
        }

    }

}

@Composable
fun DeviceColumn(devices: List<Device>, sceneViewModel: SceneViewModel) {

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
                                    // call delete device from vm with scene and deviceid
                                    sceneViewModel.deleteDevice(device.macAddress)
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
                            DeviceWithSettingsCard(device)
                        }
            }
        }
    }

}


@Composable
fun AddDialogPage1(
    devicesInScene: List<Device>,
    onDismissRequest: () -> Unit,
    onConfirmation: (device: GeneralDevice) -> Unit,
) {
    val deviceMacAddresses: List<String> = devicesInScene.mapNotNull { it?.macAddress }
    val devicesNotIn = viewModel<SceneAddDeviceViewModel>(
        factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SceneAddDeviceViewModel(
                    devicesInScene = deviceMacAddresses
                ) as T
            }
        }
    )

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
                        items(devicesNotIn.devices){device ->
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


@Composable
fun AddDialogPage2(
    onDismissRequest: () -> Unit,
    onConfirmation: (settings: Map<String, String>) -> Unit,
    selectedDevice: GeneralDevice
) {
    var settings by remember { mutableStateOf<Map<String, String>>(emptyMap())}
    val context = LocalContext.current

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                RenderDeviceSettings(
                    device = selectedDevice,
                    updateSettings = { newSettings ->
                        settings = newSettings
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick =
                        {
                            if (settings.isEmpty()){
                                Toast.makeText(context, "Select a setting!", Toast.LENGTH_SHORT).show()
                            }else
                                onConfirmation(settings)
                        },
                    ) {
                        Text("Confirm")
                    }
                }

            }
        }
    }
}

