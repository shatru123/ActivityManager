package com.lifelogger.ai.core.model

import java.time.Duration

data class AppUsageStat(
    val appName: String,
    val duration: Duration,
)
