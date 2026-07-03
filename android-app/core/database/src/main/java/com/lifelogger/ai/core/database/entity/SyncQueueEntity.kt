package com.lifelogger.ai.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val entityType: String,
    val entityId: String,
    val payloadJson: String,
    val status: String,
    val retryCount: Int,
    val nextAttemptAtEpochMillis: Long?,
)
