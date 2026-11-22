package com.devfest.wellnest.ui.stretch

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StretchScreen(
    viewModel: StretchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Stretch & Move", fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Session card
            item {
                if (uiState.isSessionActive && uiState.currentExerciseIndex >= 0) {
                    ActiveExerciseCard(
                        exercise = uiState.exercises[uiState.currentExerciseIndex],
                        remainingSeconds = uiState.remainingSeconds,
                        totalSeconds = uiState.exercises[uiState.currentExerciseIndex].durationSeconds,
                        exerciseIndex = uiState.currentExerciseIndex,
                        totalExercises = uiState.exercises.size,
                        onNext = viewModel::nextExercise,
                        onStop = viewModel::stopSession
                    )
                } else {
                    StartSessionCard(
                        sessionsToday = uiState.sessionsCompletedToday,
                        onStart = viewModel::startSession
                    )
                }
            }

            // Reminder settings
            item {
                ReminderCard(
                    enabled = uiState.reminderEnabled,
                    intervalMinutes = uiState.reminderIntervalMinutes,
                    onToggle = viewModel::toggleReminder,
                    onIntervalChange = viewModel::setReminderInterval
                )
            }

            // Exercise list
            item {
                Text("Quick Break Exercises", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }
            items(uiState.exercises) { exercise ->
                ExerciseListItem(exercise = exercise)
            }
        }
    }
}

@Composable
private fun StartSessionCard(sessionsToday: Int, onStart: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("\uD83E\uDDD8", fontSize = 48.sp)
            Text("Ready for a stretch break?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("Sessions today: $sessionsToday", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
            Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Stretch Session")
            }
        }
    }
}

@Composable
private fun ActiveExerciseCard(
    exercise: StretchExercise,
    remainingSeconds: Int,
    totalSeconds: Int,
    exerciseIndex: Int,
    totalExercises: Int,
    onNext: () -> Unit,
    onStop: () -> Unit
) {
    val progress by animateFloatAsState(
        targetValue = remainingSeconds.toFloat() / totalSeconds.toFloat(),
        label = "timer_progress"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("${exerciseIndex + 1} / $totalExercises", style = MaterialTheme.typography.labelMedium)
            Text(exercise.emoji, fontSize = 48.sp)
            Text(exercise.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(exercise.description, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f))
            Text("${remainingSeconds}s", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary)
            LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onStop) {
                    Icon(Icons.Filled.Stop, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Stop")
                }
                Button(onClick = onNext) {
                    Icon(Icons.Filled.SkipNext, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Next")
                }
            }
        }
    }
}

@Composable
private fun ReminderCard(enabled: Boolean, intervalMinutes: Int, onToggle: (Boolean) -> Unit, onIntervalChange: (Int) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Text("Stretch Reminders", style = MaterialTheme.typography.titleMedium)
                }
                Switch(checked = enabled, onCheckedChange = onToggle)
            }
            if (enabled) {
                Text("Remind every $intervalMinutes minutes", style = MaterialTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StretchData.reminderIntervals.forEach { interval ->
                        FilterChip(
                            selected = intervalMinutes == interval,
                            onClick = { onIntervalChange(interval) },
                            label = { Text("${interval}m") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExerciseListItem(exercise: StretchExercise) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(exercise.emoji, fontSize = 24.sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(exercise.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(exercise.description, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            }
            Text("${exercise.durationSeconds}s", style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary)
        }
    }
}
