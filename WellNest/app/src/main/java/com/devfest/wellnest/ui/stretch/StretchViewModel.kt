package com.devfest.wellnest.ui.stretch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StretchUiState(
    val exercises: List<StretchExercise> = StretchData.quickBreakExercises,
    val currentExerciseIndex: Int = -1,
    val isSessionActive: Boolean = false,
    val remainingSeconds: Int = 0,
    val reminderIntervalMinutes: Int = 60,
    val reminderEnabled: Boolean = false,
    val sessionsCompletedToday: Int = 0
)

@HiltViewModel
class StretchViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(StretchUiState())
    val uiState: StateFlow<StretchUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun startSession() {
        _uiState.update { it.copy(isSessionActive = true, currentExerciseIndex = 0,
            remainingSeconds = it.exercises[0].durationSeconds) }
        startTimer()
    }

    fun stopSession() {
        timerJob?.cancel()
        _uiState.update { it.copy(isSessionActive = false, currentExerciseIndex = -1, remainingSeconds = 0) }
    }

    fun nextExercise() {
        timerJob?.cancel()
        val state = _uiState.value
        val nextIndex = state.currentExerciseIndex + 1
        if (nextIndex >= state.exercises.size) {
            _uiState.update { it.copy(isSessionActive = false, currentExerciseIndex = -1,
                remainingSeconds = 0, sessionsCompletedToday = it.sessionsCompletedToday + 1) }
        } else {
            _uiState.update { it.copy(currentExerciseIndex = nextIndex,
                remainingSeconds = it.exercises[nextIndex].durationSeconds) }
            startTimer()
        }
    }

    fun setReminderInterval(minutes: Int) {
        _uiState.update { it.copy(reminderIntervalMinutes = minutes) }
    }

    fun toggleReminder(enabled: Boolean) {
        _uiState.update { it.copy(reminderEnabled = enabled) }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingSeconds > 0) {
                delay(1000)
                _uiState.update { it.copy(remainingSeconds = it.remainingSeconds - 1) }
            }
            if (_uiState.value.isSessionActive) {
                nextExercise()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
