package com.devfest.wellnest.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devfest.wellnest.data.local.entity.GoalEntity
import com.devfest.wellnest.data.local.entity.HabitEntity
import com.devfest.wellnest.data.repository.GoalRepository
import com.devfest.wellnest.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val habitRepository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalsUiState())
    val uiState: StateFlow<GoalsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                goalRepository.getActiveGoals(),
                habitRepository.getAllActiveHabits()
            ) { goals, habits ->
                _uiState.update { it.copy(goals = goals, habits = habits, isLoading = false) }
            }.collect {}
        }
    }

    fun selectTab(index: Int) = _uiState.update { it.copy(selectedTab = index) }

    fun showAddGoalDialog() = _uiState.update { it.copy(showAddGoalDialog = true) }
    fun hideAddGoalDialog() = _uiState.update { it.copy(showAddGoalDialog = false) }

    fun showAddHabitDialog() = _uiState.update { it.copy(showAddHabitDialog = true) }
    fun hideAddHabitDialog() = _uiState.update { it.copy(showAddHabitDialog = false) }

    fun addGoal(title: String, category: String, targetValue: Float, unit: String) {
        viewModelScope.launch {
            goalRepository.insertGoal(
                GoalEntity(
                    title = title,
                    category = category,
                    targetValue = targetValue,
                    unit = unit
                )
            )
            hideAddGoalDialog()
        }
    }

    fun addHabit(name: String, category: String) {
        viewModelScope.launch {
            habitRepository.insertHabit(
                HabitEntity(name = name, category = category, frequency = "DAILY")
            )
            hideAddHabitDialog()
        }
    }

    fun updateGoalProgress(goalId: Long, value: Float) {
        viewModelScope.launch {
            goalRepository.updateProgress(goalId, value)
        }
    }

    fun logHabit(habitId: Long) {
        viewModelScope.launch {
            habitRepository.logHabit(habitId, true)
        }
    }

    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch { goalRepository.deleteGoal(goal) }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch { habitRepository.deleteHabit(habit) }
    }
}
