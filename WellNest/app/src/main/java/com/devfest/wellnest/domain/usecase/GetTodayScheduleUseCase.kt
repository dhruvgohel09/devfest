package com.devfest.wellnest.domain.usecase

import com.devfest.wellnest.data.local.entity.ScheduleEntity
import com.devfest.wellnest.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {
    operator fun invoke(): Flow<ScheduleEntity?> = repository.getActiveSchedule()
}
