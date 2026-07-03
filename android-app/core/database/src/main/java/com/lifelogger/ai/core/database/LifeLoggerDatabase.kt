package com.lifelogger.ai.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lifelogger.ai.core.database.dao.ActivityLogDao
import com.lifelogger.ai.core.database.entity.ActivityLogEntity
import com.lifelogger.ai.core.database.entity.DailySummaryEntity
import com.lifelogger.ai.core.database.entity.DeviceLogEntity
import com.lifelogger.ai.core.database.entity.LocationLogEntity
import com.lifelogger.ai.core.database.entity.NotificationLogEntity
import com.lifelogger.ai.core.database.entity.SyncQueueEntity

@Database(
    entities = [
        ActivityLogEntity::class,
        LocationLogEntity::class,
        NotificationLogEntity::class,
        DeviceLogEntity::class,
        DailySummaryEntity::class,
        SyncQueueEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class LifeLoggerDatabase : RoomDatabase() {
    abstract fun activityLogDao(): ActivityLogDao
}
