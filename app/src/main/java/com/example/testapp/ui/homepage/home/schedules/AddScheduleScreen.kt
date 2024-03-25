package com.example.testapp.ui.homepage.home.schedules

import android.app.TimePickerDialog
import android.os.Build
import android.widget.TimePicker
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreen(navController: NavController){

    val context = LocalContext.current
    val scheduleViewModel = viewModel<AddScheduleViewModel>()

    val schedule = scheduleViewModel.schedule
    var scheduleName by rememberSaveable { mutableStateOf("") }
    var days by rememberSaveable { mutableStateOf(emptyList<Int>()) }
    var from by rememberSaveable { mutableStateOf("00:00") }
    var until by rememberSaveable { mutableStateOf("00:00") }

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
        HoursPickers(from = from, until = until)

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
        modifier = Modifier.padding(bottom = 6.dp)
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
            Button(
                onClick = { onDaySelected(i) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (isSelected) Color.Black else Color.White
                )
            ) {
                Text(text = day)
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HoursPickers(from: String, until: String){

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TimePickerColumn(title = "From", onTimeSelected = {})

        TimePickerColumn(title = "Until", onTimeSelected = {})
    }
    Spacer(modifier = Modifier.height(16.dp))

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePickerColumn(title: String, initialTime: String = LocalTime.now().toString(), onTimeSelected: (LocalTime) -> Unit) {
    var selectedTime by remember { mutableStateOf(LocalTime.parse(initialTime)) }
    var showTimePicker by remember { mutableStateOf(false) }
    val state = ""
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()


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
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White
            )
        ) {
            Text(
                text = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

//        if (showTimePicker) {
//            TimePickerDialog(
//                onCancel = { showTimePicker = false },
//                onConfirm = {
//                    val cal = Calendar.getInstance()
//                    cal.set(Calendar.HOUR_OF_DAY, state.hour)
//                    cal.set(Calendar.MINUTE, state.minute)
//                    cal.isLenient = false
//                    snackScope.launch {
//                        snackState.showSnackbar("Entered time: ${formatter.format(cal.time)}")
//                    }
//                    showTimePicker = false
//                },
//            ) {
//                TimePicker(state = state)
//            }
//        }
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