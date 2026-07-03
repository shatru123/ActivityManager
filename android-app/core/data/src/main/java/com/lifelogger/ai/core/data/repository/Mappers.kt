package com.lifelogger.ai.core.data.repository

import com.lifelogger.ai.core.database.entity.ActivityLogEntity
import com.lifelogger.ai.core.model.ActivityLog
import com.lifelogger.ai.core.model.UserSession
import com.lifelogger.ai.core.network.model.AuthResponseDto
import java.time.Duration
import java.time.Instant

internal fun ActivityLogEntity.asExternalModel(): ActivityLog =
    ActivityLog(
        id = id,
        appName = appName,
        packageName = packageName,
        startTime = Instant.ofEpochMilli(startTimeEpochMillis),
        endTime = Instant.ofEpochMilli(endTimeEpochMillis),
        duration = Duration.ofMillis(durationMillis),
    )

internal fun AuthResponseDto.asSession(): UserSession =
    UserSession(
        userId = userId,
        fullName = fullName,
        email = email,
        accessToken = accessToken,
        refreshToken = refreshToken,
        accessTokenExpiresAtEpochMillis = Instant.parse(accessTokenExpiresAtUtc).toEpochMilli(),
    )
