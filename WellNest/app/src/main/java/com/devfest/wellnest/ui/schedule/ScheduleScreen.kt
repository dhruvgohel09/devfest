package com.devfest.wellnest.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Schedule", fontWeight = FontWeight.Bold) })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.saveSchedule() },
                icon = { Icon(Icons.Filled.Save, contentDescription = null) },
                text = { Text("Save") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Work Hours", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TimeRow(
                        label = "Work Start",
                        hour = uiState.workStartHour,
                        minute = uiState.workStartMinute,
                        onTimeChange = viewModel::updateWorkStart
                    )
                    HorizontalDivider()
                    TimeRow(
                        label = "Work End",
                        hour = uiState.workEndHour,
                        minute = uiState.workEndMinute,
                        onTimeChange = viewModel::updateWorkEnd
                    )
                    HorizontalDivider()
                    TimeRow(
                        label = "Lunch Start",
                        hour = uiState.lunchStartHour,
                        minute = uiState.lunchStartMinute,
                        onTimeChange = viewModel::updateLunchStart
                    )
                }
            }

            Text("Options", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Sync to Google Calendar", style = MaterialTheme.typography.bodyLarge)
                        Text("Add schedule blocks to your calendar", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                    Switch(
                        checked = uiState.syncToCalendar,
                        onCheckedChange = viewModel::toggleSyncToCalendar
                    )
                }
            }

            if (uiState.saveSuccess) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        "Schedule saved successfully!",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeRow(label: String, hour: Int, minute: Int, onTimeChange: (Int, Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = "%02d:%02d".format(hour, minute),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
