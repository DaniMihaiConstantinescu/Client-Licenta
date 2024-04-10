package com.example.testapp.ui.homepage.home.schedules

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.testapp.ui.homepage.home.common.RenderDeviceSettings
import com.example.testapp.utils.dataClasses.general.Device
import com.example.testapp.utils.dataClasses.general.GeneralDevice
import com.example.testapp.utils.viewModels.homeScreen.Schedules.AddScheduleViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddScheduleScreen(navController: NavController){

    val context = LocalContext.current
    val scheduleViewModel = viewModel<AddScheduleViewModel>()

    val schedule = scheduleViewModel.schedule
    var scheduleName by rememberSaveable { mutableStateOf("") }
    var days by rememberSaveable { mutableStateOf(emptyList<Int>()) }
    var from by rememberSaveable { mutableStateOf(mapOf("h" to 0, "m" to 0)) }
    var until by rememberSaveable { mutableStateOf(mapOf("h" to 0, "m" to 0)) }

    var showAddDialog1 by remember { mutableStateOf(false) }
    var showAddDialog2 by remember { mutableStateOf(false) }
    var selectedAddDevice by remember { mutableStateOf(
        GeneralDevice(
        deviceMAC = "",
        hubMac = "",
        name = "",
        type = ""
    )
    ) }

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
            Text(text = "Add Schedule", style = MaterialTheme.typography.headlineMedium)
        }
        TextField(
            modifier = Modifier
                .clickable { }
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            value = scheduleName,
            onValueChange = { scheduleName = it },
            label = { Text("Schedule Name") },
            singleLine = true
        )

        DaysRow(
            selectedDays = days,
            onDaySelected = { selectedDay ->
                days = if (selectedDay in days) {
                    days - selectedDay
                } else {
                    days + selectedDay
                }
            }
        )
        HoursPickers(
            from = from,
            until = until,
            updateFrom = { time -> from = time },
            updateUntil = { time -> until = time },
        )

        AddButtonRow(onClick = { showAddDialog1 = true }, text = "Add Device")
        DeviceColumn01(schedule.devices, scheduleViewModel)

        if (showAddDialog1) {
            scheduleViewModel.getAllDevicesFromHub()
            AddDialogPage01(
                viewModel = scheduleViewModel,
                onDismissRequest = { showAddDialog1 = false },
                onConfirmation = { addDevice ->
                    showAddDialog1 = false
                    showAddDialog2 = true
                    selectedAddDevice = addDevice
                }
            )
        }
        if (showAddDialog2){
            AddDialogPage02(
                onDismissRequest = { showAddDialog2 = false },
                onConfirmation = { settings ->
                    showAddDialog2 = false
                    scheduleViewModel.addDeviceToSchedule(
                        newDevice = Device(
                            selectedAddDevice.deviceMAC,
                            settings,
                        )
                    )
                },
                selectedDevice = selectedAddDevice
            )
        }


        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                if ( scheduleName == "" )
                    Toast.makeText(context, "Add a name to the schedule", Toast.LENGTH_SHORT).show()
                else{
                    scheduleViewModel.createSchedule(scheduleName, "11:00", "12:00", days) { response ->
                        if (response.isSuccessful) {
                            navController.navigate("home")
                        } else {
                            Toast.makeText(context, "Error while creating the schedule", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(12.dp) ,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            )
        ) {
            Text(
                text= "Create",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 22.dp)
            )
        }

    }

}

@Composable
fun DaysRow(selectedDays: List<Int>, onDaySelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0..6) {
            val day = when (i) {
                0 -> "S"
                1 -> "M"
                2 -> "T"
                3 -> "W"
                4 -> "T"
                5 -> "F"
                else -> "S"
            }
            val isSelected = selectedDays.contains(i)

            Text(
                text = day,
                Modifier
                    .clickable { onDaySelected(i) },
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HoursPickers(
    from: Map<String, Int>,
    until: Map<String, Int>,
    updateFrom: (time: Map<String, Int>) -> Unit,
    updateUntil: (time: Map<String, Int>) -> Unit
){

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TimePickerColumn(
            title = "From",
            from,
            updateTime = { h, m -> updateFrom(mapOf("h" to h, "m" to m)) }
        )

        TimePickerColumn(
            title = "Until",
            until,
            updateTime = { h, m -> updateUntil(mapOf("h" to h, "m" to m)) }
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePickerColumn(
    title: String,
    time: Map<String, Int>,
    updateTime: (h: Int, m: Int) -> Unit
) {

    var showTimePicker by remember { mutableStateOf(false) }

    Column {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = { showTimePicker = true },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "${time["h"]?.toString()?.padStart(2, '0')}:${time["m"]?.toString()?.padStart(2, '0')}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        
        if (showTimePicker)
            TimePickerDialog(
                onCancel = { showTimePicker = false },
                onConfirm = { h, m ->
                    showTimePicker = false
                    updateTime(h,m)
                },
                initialHour = time["h"] ?: 0,
                initialMinute = time["m"] ?: 0
            )

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialHour: Int = 0,
    initialMinute: Int = 0,
    onCancel: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val timeState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    Dialog(
        onDismissRequest = onCancel
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(state = timeState)
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancel ) {
                        Text(text = "Dismiss")
                    }
                    TextButton(onClick = {
                        onConfirm(timeState.hour, timeState.minute)
                    }) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }

}



@Composable
fun DeviceColumn01(devices: List<Device>, scheduleViewModel: AddScheduleViewModel) {

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
                                // call delete device from vm with schedule and deviceId
                                scheduleViewModel.deleteDevice(device.macAddress)
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
                        DeviceWithSettingsCard01(device)
                    }
            }
        }
    }

}

@Composable
fun DeviceWithSettingsCard01(device: Device){

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
            Text(text = device.macAddress, color = Color.White , style = MaterialTheme.typography.titleLarge)
            Text(
                text = device.settings.values.first(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }

}

@Composable
fun AddDialogPage01(
    viewModel: AddScheduleViewModel,
    onDismissRequest: () -> Unit,
    onConfirmation: (device: GeneralDevice) -> Unit,
) {
    viewModel.getAllDevicesFromHub()
    val devicesToAdd = viewModel.devicesToAdd.filter { newDevice ->
        viewModel.schedule.devices.none { existingDevice -> existingDevice.macAddress == newDevice.deviceMAC }
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
                        if (devicesToAdd.isEmpty()){
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                    text = "There is no device to add"
                                )
                            }
                        }
                        else items(devicesToAdd){device ->
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
fun AddDialogPage02(
    onDismissRequest: () -> Unit,
    onConfirmation: (settings: Map<String, String>) -> Unit,
    selectedDevice: GeneralDevice
) {
    var settings by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
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