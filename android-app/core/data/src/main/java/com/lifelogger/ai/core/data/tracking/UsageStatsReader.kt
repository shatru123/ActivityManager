package com.lifelogger.ai.core.data.tracking

import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import com.lifelogger.ai.core.database.entity.ActivityLogEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsageStatsReader @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val usageStatsManager: UsageStatsManager =
        context.getSystemService(UsageStatsManager::class.java)
    private val appOpsManager: AppOpsManager =
        context.getSystemService(AppOpsManager::class.java)
    private val packageManager: PackageManager = context.packageManager

    fun hasUsagePermission(): Boolean {
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOpsManager.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName,
            )
        } else {
            @Suppress("DEPRECATION")
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName,
            )
        }

        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun readDay(day: LocalDate): List<ActivityLogEntity> {
        if (!hasUsagePermission()) return emptyList()

        val zoneId = ZoneId.systemDefault()
        val startOfDayMillis = day.atStartOfDay(zoneId).toInstant().toEpochMilli()
        val endOfDayMillis = day.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli()
        val usageEvents = usageStatsManager.queryEvents(startOfDayMillis, endOfDayMillis)
        val activeSessions = mutableMapOf<String, Long>()
        val collectedLogs = mutableListOf<ActivityLogEntity>()
        val event = UsageEvents.Event()

        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            val packageName = event.packageName ?: continue
            if (packageName == context.packageName) continue

            when (event.eventType) {
                UsageEvents.Event.ACTIVITY_RESUMED,
                UsageEvents.Event.MOVE_TO_FOREGROUND -> {
                    activeSessions[packageName] = event.timeStamp
                }

                UsageEvents.Event.ACTIVITY_PAUSED,
                UsageEvents.Event.MOVE_TO_BACKGROUND -> {
                    val startedAt = activeSessions.remove(packageName) ?: continue
                    val duration = event.timeStamp - startedAt
                    if (duration < 1_000L) continue

                    collectedLogs += ActivityLogEntity(
                        appName = resolveAppName(packageName),
                        packageName = packageName,
                        startTimeEpochMillis = startedAt,
                        endTimeEpochMillis = event.timeStamp,
                        durationMillis = duration,
                    )
                }
            }
        }

        activeSessions.forEach { (packageName, startedAt) ->
            val clampedEnd = minOf(System.currentTimeMillis(), endOfDayMillis)
            val duration = clampedEnd - startedAt
            if (duration >= 1_000L) {
                collectedLogs += ActivityLogEntity(
                    appName = resolveAppName(packageName),
                    packageName = packageName,
                    startTimeEpochMillis = startedAt,
                    endTimeEpochMillis = clampedEnd,
                    durationMillis = duration,
                )
            }
        }

        return collectedLogs
            .sortedBy { it.startTimeEpochMillis }
            .distinctBy { "${it.packageName}-${it.startTimeEpochMillis}-${it.endTimeEpochMillis}" }
    }

    private fun resolveAppName(packageName: String): String =
        runCatching {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        }.getOrDefault(packageName)
}
