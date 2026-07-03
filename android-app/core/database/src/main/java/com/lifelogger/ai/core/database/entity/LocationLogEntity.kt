package com.lifelogger.ai.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_logs")
data class LocationLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val speedMetersPerSecond: Float?,
    val accuracyMeters: Float?,
    val address: String?,
    val capturedAtEpochMillis: Long,
)
