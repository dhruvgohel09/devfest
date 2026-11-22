package com.devfest.wellnest.domain.usecase

import com.devfest.wellnest.data.local.entity.HabitEntity
import com.devfest.wellnest.data.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayHabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    operator fun invoke(): Flow<List<HabitEntity>> = habitRepository.getAllActiveHabits()
}
