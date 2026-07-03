package com.lifelogger.ai.core.common.util

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val timeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm")

fun Instant.toTimelineTime(zoneId: ZoneId = ZoneId.systemDefault()): String =
    timeFormatter.format(atZone(zoneId))

fun Duration.toReadableDuration(): String {
    val hours = toHours()
    val minutes = toMinutes() % 60
    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        else -> "${toMinutes()}m"
    }
}
