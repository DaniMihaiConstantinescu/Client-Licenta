package com.example.testapp.ui.homepage.home.schedules

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
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
import com.example.testapp.utils.funcs.parseTime
import com.example.testapp.utils.viewModels.homeScreen.Schedules.ScheduleAddDeviceViewModel
import com.example.testapp.utils.viewModels.homeScreen.Schedules.ScheduleViewModel

@Composable
fun ScheduleScreen(scheduleId: String){

    val scheduleViewModel = viewModel<ScheduleViewModel>(
        factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ScheduleViewModel(
                    scheduleId = scheduleId,
                ) as T
            }
        }
    )
    val schedule = scheduleViewModel.schedule
    var text by rememberSaveable { mutableStateOf("") }

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
            Text(text = schedule.scheduleName, style = MaterialTheme.typography.headlineMedium)
        }
        TextField(
            modifier = Modifier
                .clickable { }
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            value = text,
            onValueChange = { text = it },
            label = { Text("Schedule Name") },
            singleLine = true
        )

        DaysRow(selectedDays = schedule.days)
        HoursPickers(from = parseTime(schedule.from), until = parseTime(schedule.until))

        AddButtonRow(onClick = { showAddDialog1 = true }, text = "Add Device")
        DeviceColumn(schedule.devices, scheduleViewModel)

        if (showAddDialog1) {
            AddDialogPage1(
                devicesInSchedule = schedule.devices,
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

    }

}

@Composable
fun DaysRow(selectedDays: List<Int>) {
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
                    .clickable {  },
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
            )
        }
    }
}
@Composable
fun HoursPickers(
    from: Map<String, Int>,
    until: Map<String, Int>,
){

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TimePickerColumn(
            title = "From",
            from
        )

        TimePickerColumn(
            title = "Until",
            until
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

}

@Composable
fun TimePickerColumn(
    title: String,
    time: Map<String, Int>,
) {


    Column {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = {  },
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

    }
}

@Composable
fun DeviceColumn(devices: List<Device>, scheduleViewModel: ScheduleViewModel) {

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
                        DeviceWithSettingsCard(device)
                    }
            }
        }
    }

}


@Composable
fun AddDialogPage1(
    devicesInSchedule: List<Device>,
    onDismissRequest: () -> Unit,
    onConfirmation: (device: GeneralDevice) -> Unit,
) {
    val deviceMacAddresses: List<String> = devicesInSchedule.mapNotNull { it?.macAddress }
    val devicesNotIn = viewModel<ScheduleAddDeviceViewModel>(
        factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ScheduleAddDeviceViewModel(
                    devicesInSchedule = deviceMacAddresses
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
                        if (devicesNotIn.devices.isEmpty()){
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
                        else items(devicesNotIn.devices){device ->
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

