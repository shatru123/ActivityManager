package com.lifelogger.ai.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_summaries")
data class DailySummaryEntity(
    @PrimaryKey val dateIso: String,
    val summaryText: String?,
    val topAppName: String?,
    val topAppDurationMillis: Long?,
    val generatedAtEpochMillis: Long?,
)
