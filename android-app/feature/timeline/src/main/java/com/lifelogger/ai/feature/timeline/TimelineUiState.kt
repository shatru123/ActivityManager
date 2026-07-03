package com.lifelogger.ai.feature.timeline

import com.lifelogger.ai.core.model.ActivityLog
import com.lifelogger.ai.core.model.AppUsageStat
import java.time.LocalDate

data class TimelineUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val userName: String = "",
    val hasUsagePermission: Boolean = false,
    val isRefreshing: Boolean = false,
    val entries: List<ActivityLog> = emptyList(),
    val topApps: List<AppUsageStat> = emptyList(),
    val totalTrackedMinutes: Long = 0,
    val errorMessage: String? = null,
)
