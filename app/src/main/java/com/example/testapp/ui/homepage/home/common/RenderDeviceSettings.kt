package com.example.testapp.ui.homepage.home.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.testapp.utils.dataClasses.general.GeneralDevice
import java.util.Locale


enum class Types(val type:String, val setting: String){
    AC("ac", "Temperature"),
    DEHUMIDIFIER("dehumidifier", "Humidity"),
    LIGHT("light", "ON/OFF"),
    SHUTTER("shutter", "UP/DOWN"),
    SPRINKLER("sprinkler", "")
}

@Composable
fun RenderDeviceSettings(
    device: GeneralDevice,
    updateSettings: (newSettings: Map<String,String>) -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = device.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Type: " + device.type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )
        SettingsRederer(device = device, updateSettings)
    }
}

@Composable
fun SettingsRederer(
    device: GeneralDevice,
    updateSettings: (settings: Map<String,String>) -> Unit
){
    when (device.type){
        Types.AC.type -> {

            LaunchedEffect(Unit) {
                updateSettings(mapOf("temperature" to "18"))
            }
            var sliderPosition by remember { mutableStateOf(18f) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            ) {
                Text(text = Types.AC.setting)

                Column {
                    Slider(
                        value = sliderPosition,
                        onValueChange =
                        {
                            sliderPosition = it
                            updateSettings(mapOf("temperature" to sliderPosition.toString()))
                        },
                        steps = 19,
                        valueRange = 18f..28f,
                    )
                    Text(
                        text = "$sliderPosition\u00B0C",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Types.DEHUMIDIFIER.type -> {

            LaunchedEffect(Unit) {
                updateSettings(mapOf("humidity" to "30"))
            }
            var sliderPosition by remember { mutableStateOf(30) }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = Types.DEHUMIDIFIER.setting)

                Column {
                    Slider(
                        value = sliderPosition.toFloat(),
                        onValueChange =
                        {
                            sliderPosition = it.toInt()
                            updateSettings(mapOf("humidity" to it.toInt().toString()))
                        },
                        steps = 4,
                        valueRange = 30f..80f,
                    )
                    Text(
                        text = "$sliderPosition%",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Types.LIGHT.type -> {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                var selectedButton by remember { mutableStateOf("") }
                Column(Modifier.selectableGroup())
                {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedButton == "ON",
                            onClick =
                            {
                                selectedButton = "ON"
                                updateSettings(mapOf("state" to "on"))
                            },
                            modifier = Modifier.semantics { contentDescription = "ON" }
                        )
                        Text(text = "ON")
                    }

                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        RadioButton(
                            selected = selectedButton == "OFF",
                            onClick =
                            {
                                selectedButton = "OFF"
                                updateSettings(mapOf("state" to "off"))
                            },
                            modifier = Modifier.semantics { contentDescription = "OFF" }
                        )
                        Text(text = "OFF")
                    }
                }

            }
        }
        Types.SHUTTER.type -> {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                var selectedButton by remember { mutableStateOf("") }
                Column(Modifier.selectableGroup())
                {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedButton == "UP",
                            onClick =
                            {
                                selectedButton = "UP"
                                updateSettings(mapOf("state" to "up"))
                            },
                            modifier = Modifier.semantics { contentDescription = "UP" }
                        )
                        Text(text = "UP")
                    }

                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        RadioButton(
                            selected = selectedButton == "DOWN",
                            onClick =
                            {
                                selectedButton = "DOWN"
                                updateSettings(mapOf("state" to "down"))
                            },
                            modifier = Modifier.semantics { contentDescription = "DOWN" }
                        )
                        Text(text = "DOWN")
                    }
                }
            }
        }

    }
}