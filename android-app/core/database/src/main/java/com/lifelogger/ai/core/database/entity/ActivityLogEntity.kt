package com.lifelogger.ai.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "activity_logs",
    indices = [
        Index(value = ["startTimeEpochMillis"]),
        Index(value = ["packageName", "startTimeEpochMillis"]),
    ],
)
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val appName: String,
    val packageName: String,
    val startTimeEpochMillis: Long,
    val endTimeEpochMillis: Long,
    val durationMillis: Long,
)
