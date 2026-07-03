package com.lifelogger.ai.core.domain.usecase

import com.lifelogger.ai.core.domain.repository.TimelineRepository
import com.lifelogger.ai.core.model.ActivityLog
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveTimelineUseCase @Inject constructor(
    private val timelineRepository: TimelineRepository,
) {
    operator fun invoke(day: LocalDate): Flow<List<ActivityLog>> =
        timelineRepository.observeDay(day)
}

class RefreshTimelineUseCase @Inject constructor(
    private val timelineRepository: TimelineRepository,
) {
    suspend operator fun invoke(day: LocalDate): Result<Unit> =
        timelineRepository.refreshDay(day)
}

class HasUsagePermissionUseCase @Inject constructor(
    private val timelineRepository: TimelineRepository,
) {
    operator fun invoke(): Boolean = timelineRepository.hasUsagePermission()
}
