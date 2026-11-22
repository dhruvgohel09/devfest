package com.devfest.wellnest.data.repository

import com.devfest.wellnest.data.local.dao.GoalDao
import com.devfest.wellnest.data.local.entity.GoalEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepository @Inject constructor(
    private val goalDao: GoalDao
) {
    fun getActiveGoals(): Flow<List<GoalEntity>> = goalDao.getActiveGoals()
    fun getAllGoals(): Flow<List<GoalEntity>> = goalDao.getAllGoals()
    suspend fun insertGoal(goal: GoalEntity) = goalDao.insertGoal(goal)
    suspend fun updateGoal(goal: GoalEntity) = goalDao.updateGoal(goal)
    suspend fun updateProgress(goalId: Long, value: Float) = goalDao.updateGoalProgress(goalId, value)
    suspend fun deleteGoal(goal: GoalEntity) = goalDao.deleteGoal(goal)
}
