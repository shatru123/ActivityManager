package com.lifelogger.ai.core.data.repository

import com.lifelogger.ai.core.common.di.IoDispatcher
import com.lifelogger.ai.core.data.tracking.UsageStatsReader
import com.lifelogger.ai.core.database.dao.ActivityLogDao
import com.lifelogger.ai.core.datastore.AppPreferences
import com.lifelogger.ai.core.domain.repository.TimelineRepository
import com.lifelogger.ai.core.model.ActivityLog
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
class DefaultTimelineRepository @Inject constructor(
    private val activityLogDao: ActivityLogDao,
    private val appPreferences: AppPreferences,
    private val usageStatsReader: UsageStatsReader,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TimelineRepository {
    override fun observeDay(day: LocalDate): Flow<List<ActivityLog>> {
        val (startOfDay, endOfDay) = day.toDayBounds()
        return activityLogDao.observeDay(startOfDay, endOfDay)
            .map { logs -> logs.map { it.asExternalModel() } }
    }

    override fun hasUsagePermission(): Boolean = usageStatsReader.hasUsagePermission()

    override suspend fun refreshDay(day: LocalDate): Result<Unit> = runCatching {
        withContext(ioDispatcher) {
            val (startOfDay, endOfDay) = day.toDayBounds()
            val logs = usageStatsReader.readDay(day)
            activityLogDao.replaceDay(startOfDay, endOfDay, logs)
            appPreferences.updateLastUsageImportAt(System.currentTimeMillis())
        }
    }

    private fun LocalDate.toDayBounds(): Pair<Long, Long> {
        val zoneId = ZoneId.systemDefault()
        val startOfDay = atStartOfDay(zoneId).toInstant().toEpochMilli()
        val endOfDay = plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli()
        return startOfDay to endOfDay
    }
}
