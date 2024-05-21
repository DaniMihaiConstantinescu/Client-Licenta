package com.example.testapp.utils.funcs

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.testapp.utils.enums.IconsIds

@Composable
fun RenderDeviceIcon(deviceType: String){
    when (deviceType){
        "ac" -> Icon(
            painter = painterResource(IconsIds.AC.drawableResId),
            contentDescription = IconsIds.AC.iconName
        )
        "dehumidifier" -> Icon(
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
