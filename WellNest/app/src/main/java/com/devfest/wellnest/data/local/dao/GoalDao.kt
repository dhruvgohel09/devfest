package com.devfest.wellnest.data.local.dao

import androidx.room.*
import com.devfest.wellnest.data.local.entity.GoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals ORDER BY createdAt DESC")
    fun getAllGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE isCompleted = 0 ORDER BY deadline ASC")
    fun getActiveGoals(): Flow<List<GoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity): Long

    @Update
    suspend fun updateGoal(goal: GoalEntity)

    @Delete
    suspend fun deleteGoal(goal: GoalEntity)

    @Query("UPDATE goals SET currentValue = :value WHERE id = :goalId")
    suspend fun updateGoalProgress(goalId: Long, value: Float)
}
