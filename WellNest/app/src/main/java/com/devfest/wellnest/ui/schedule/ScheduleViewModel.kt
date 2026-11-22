package com.devfest.wellnest.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devfest.wellnest.data.local.entity.ScheduleEntity
import com.devfest.wellnest.data.repository.ScheduleRepository
import com.devfest.wellnest.domain.usecase.GetTodayScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val getTodayScheduleUseCase: GetTodayScheduleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getTodayScheduleUseCase().collect { schedule ->
                schedule?.let {
                    _uiState.update { state ->
                        state.copy(
                            activeSchedule = it,
                            workStartHour = it.workStartHour,
                            workStartMinute = it.workStartMinute,
                            workEndHour = it.workEndHour,
                            workEndMinute = it.workEndMinute,
                            lunchStartHour = it.lunchStartHour,
                            lunchStartMinute = it.lunchStartMinute,
                            lunchDurationMinutes = it.lunchDurationMinutes,
                            eveningBreakHour = it.eveningBreakHour,
                            eveningBreakMinute = it.eveningBreakMinute,
                            eveningBreakDurationMinutes = it.eveningBreakDurationMinutes,
                            repeatDays = it.repeatDays
                        )
                    }
                }
            }
        }
    }

    fun updateWorkStart(hour: Int, minute: Int) {
        _uiState.update { it.copy(workStartHour = hour, workStartMinute = minute) }
    }

    fun updateWorkEnd(hour: Int, minute: Int) {
        _uiState.update { it.copy(workEndHour = hour, workEndMinute = minute) }
    }

    fun updateLunchStart(hour: Int, minute: Int) {
        _uiState.update { it.copy(lunchStartHour = hour, lunchStartMinute = minute) }
    }

    fun toggleSyncToCalendar(sync: Boolean) {
        _uiState.update { it.copy(syncToCalendar = sync) }
    }

    fun saveSchedule() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val state = _uiState.value
                val entity = ScheduleEntity(
                    workStartHour = state.workStartHour,
                    workStartMinute = state.workStartMinute,
                    workEndHour = state.workEndHour,
                    workEndMinute = state.workEndMinute,
                    lunchStartHour = state.lunchStartHour,
                    lunchStartMinute = state.lunchStartMinute,
                    lunchDurationMinutes = state.lunchDurationMinutes,
                    eveningBreakHour = state.eveningBreakHour,
                    eveningBreakMinute = state.eveningBreakMinute,
                    eveningBreakDurationMinutes = state.eveningBreakDurationMinutes,
                    repeatDays = state.repeatDays
                )
                scheduleRepository.saveSchedule(entity, state.syncToCalendar)
                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }
}
