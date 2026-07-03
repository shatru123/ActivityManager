package com.lifelogger.ai.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.lifelogger.ai.core.database.entity.ActivityLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDao {
    @Query(
        """
        SELECT * FROM activity_logs
        WHERE startTimeEpochMillis >= :startOfDayMillis
          AND startTimeEpochMillis < :endOfDayMillis
        ORDER BY startTimeEpochMillis ASC
        """
    )
    fun observeDay(
        startOfDayMillis: Long,
        endOfDayMillis: Long,
    ): Flow<List<ActivityLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<ActivityLogEntity>)

    @Query(
        """
        DELETE FROM activity_logs
        WHERE startTimeEpochMillis >= :startOfDayMillis
          AND startTimeEpochMillis < :endOfDayMillis
        """
    )
    suspend fun deleteDayRange(
        startOfDayMillis: Long,
        endOfDayMillis: Long,
    )

    @Transaction
    suspend fun replaceDay(
        startOfDayMillis: Long,
        endOfDayMillis: Long,
        logs: List<ActivityLogEntity>,
    ) {
        deleteDayRange(startOfDayMillis, endOfDayMillis)
        insertAll(logs)
    }
}
