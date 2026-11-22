package com.devfest.wellnest.ui.goals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devfest.wellnest.data.local.entity.GoalEntity
import com.devfest.wellnest.data.local.entity.HabitEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Goals & Habits", fontWeight = FontWeight.Bold) })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (uiState.selectedTab == 0) viewModel.showAddGoalDialog()
                    else viewModel.showAddHabitDialog()
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = uiState.selectedTab) {
                Tab(
                    selected = uiState.selectedTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    text = { Text("Goals") }
                )
                Tab(
                    selected = uiState.selectedTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    text = { Text("Habits") }
                )
            }

            when (uiState.selectedTab) {
                0 -> GoalsList(goals = uiState.goals, onDelete = viewModel::deleteGoal, onProgress = viewModel::updateGoalProgress)
                1 -> HabitsList(habits = uiState.habits, onDelete = viewModel::deleteHabit, onLog = viewModel::logHabit)
            }
        }
    }

    if (uiState.showAddGoalDialog) {
        AddGoalDialog(onDismiss = viewModel::hideAddGoalDialog, onAdd = viewModel::addGoal)
    }

    if (uiState.showAddHabitDialog) {
        AddHabitDialog(onDismiss = viewModel::hideAddHabitDialog, onAdd = viewModel::addHabit)
    }
}

@Composable
private fun GoalsList(
    goals: List<GoalEntity>,
    onDelete: (GoalEntity) -> Unit,
    onProgress: (Long, Float) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (goals.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No goals yet. Add your first goal!", style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
        }
        items(goals) { goal ->
            GoalCard(goal = goal, onDelete = { onDelete(goal) })
        }
    }
}

@Composable
private fun GoalCard(goal: GoalEntity, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(goal.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("${goal.category} • ${goal.unit}", style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
            LinearProgressIndicator(
                progress = { (goal.currentValue / goal.targetValue).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "${goal.currentValue} / ${goal.targetValue} ${goal.unit}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun HabitsList(
    habits: List<HabitEntity>,
    onDelete: (HabitEntity) -> Unit,
    onLog: (Long) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (habits.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No habits yet. Build your first habit!", style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
        }
        items(habits) { habit ->
            HabitCard(habit = habit, onDelete = { onDelete(habit) }, onLog = { onLog(habit.id) })
        }
    }
}

@Composable
private fun HabitCard(habit: HabitEntity, onDelete: () -> Unit, onLog: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(habit.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("${habit.category} • Streak: ${habit.streak}", style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
            TextButton(onClick = onLog) { Text("Log") }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun AddGoalDialog(onDismiss: () -> Unit, onAdd: (String, String, Float, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("FITNESS") }
    var target by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("steps") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Goal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = target, onValueChange = { target = it }, label = { Text("Target Value") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = unit, onValueChange = { unit = it }, label = { Text("Unit") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            TextButton(onClick = { onAdd(title, category, target.toFloatOrNull() ?: 0f, unit) }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun AddHabitDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("HYDRATION") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Habit") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Habit Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            TextButton(onClick = { onAdd(name, category) }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
