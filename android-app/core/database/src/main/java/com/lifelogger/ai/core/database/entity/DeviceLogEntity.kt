package com.lifelogger.ai.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_logs")
data class DeviceLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val batteryPercentage: Int?,
    val chargingState: String?,
    val wifiEnabled: Boolean?,
    val bluetoothEnabled: Boolean?,
    val networkType: String?,
    val recordedAtEpochMillis: Long,
)
