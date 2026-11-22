package com.devfest.wellnest.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devfest.wellnest.domain.usecase.GetTodayHabitsUseCase
import com.devfest.wellnest.domain.usecase.GetTodayScheduleUseCase
import com.devfest.wellnest.domain.usecase.SyncCalendarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getTodayScheduleUseCase: GetTodayScheduleUseCase,
    private val getTodayHabitsUseCase: GetTodayHabitsUseCase,
    private val syncCalendarUseCase: SyncCalendarUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, greeting = getGreeting()) }

            combine(
                getTodayScheduleUseCase(),
                getTodayHabitsUseCase()
            ) { schedule, habits ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        activeSchedule = schedule,
                        todayHabits = habits
                    )
                }
            }.collect {}
        }

        viewModelScope.launch {
            try {
                val events = syncCalendarUseCase.getTodayEvents()
                _uiState.update { it.copy(calendarEvents = events) }
            } catch (e: Exception) {
                // Calendar not configured yet — silently ignore
            }
        }
    }

    private fun getGreeting(): String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good morning"
            in 12..17 -> "Good afternoon"
            else -> "Good evening"
        }
    }
}
