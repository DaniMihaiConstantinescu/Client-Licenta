package com.example.testapp.ui.homepage.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.testapp.utils.dataClasses.homeScreen.Schedule
import com.example.testapp.utils.viewModels.homeScreen.Schedules.ScheduleCardViewModel

@Composable
fun ScheduleCard(
    schedule: Schedule,
    navController: NavController
) {
    var checked by remember { mutableStateOf(schedule.isActive) }
    val scheduleViewModel = viewModel<ScheduleCardViewModel>()
    var context = LocalContext.current

    Card(
        modifier = Modifier
            .clickable { navController.navigate("schedule/${schedule.scheduleId}") }
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = schedule.scheduleName, color = MaterialTheme.colorScheme.primary , style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        scheduleViewModel.toggleSchedule(schedule.scheduleId) { success ->
                            if (success) {
                                checked = it
                            } else {
                                Toast.makeText(context, "An error occurred while toggling the schedule ", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            }
            
            Text(text = formatDaysOfWeek(schedule.days), modifier = Modifier.padding(bottom = 2.dp))
            Text(text = "${schedule.from} - ${schedule.until}", color = Color.White, style = MaterialTheme.typography.titleLarge)
        }
    }
}

fun formatDaysOfWeek(days: List<Int>): String {
    return when {
        days.size == 7 && days.all { it in 0..6 } -> "Every day"
        days.size == 1 && days[0] in 0..6 -> "Every ${getDayOfWeekName(days[0])}"
        else -> {
            val dayNames = days.mapNotNull { getDayOfWeekName(it) }
            dayNames.joinToString(", ")
        }
    }
}

fun getDayOfWeekName(day: Int): String? {
    return when (day) {
        0 -> "Sunday"
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        else -> null
    }
}

