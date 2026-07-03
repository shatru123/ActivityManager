package com.lifelogger.ai.core.domain.repository

import com.lifelogger.ai.core.model.ActivityLog
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

interface TimelineRepository {
    fun observeDay(day: LocalDate): Flow<List<ActivityLog>>

    fun hasUsagePermission(): Boolean

    suspend fun refreshDay(day: LocalDate): Result<Unit>
}
