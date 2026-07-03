package com.lifelogger.ai.core.model

import java.time.Duration
import java.time.Instant

data class ActivityLog(
    val id: Long = 0,
    val appName: String,
    val packageName: String,
    val startTime: Instant,
    val endTime: Instant,
    val duration: Duration,
)
