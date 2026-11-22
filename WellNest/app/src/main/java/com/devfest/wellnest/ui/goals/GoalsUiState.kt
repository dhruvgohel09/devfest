package com.devfest.wellnest.ui.goals

import com.devfest.wellnest.data.local.entity.GoalEntity
import com.devfest.wellnest.data.local.entity.HabitEntity

data class GoalsUiState(
    val isLoading: Boolean = false,
    val goals: List<GoalEntity> = emptyList(),
    val habits: List<HabitEntity> = emptyList(),
    val selectedTab: Int = 0, // 0 = Goals, 1 = Habits
    val showAddGoalDialog: Boolean = false,
    val showAddHabitDialog: Boolean = false,
    val error: String? = null
)
