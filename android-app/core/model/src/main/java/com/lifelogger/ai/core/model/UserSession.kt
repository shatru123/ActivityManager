package com.lifelogger.ai.core.model

data class UserSession(
    val userId: String,
    val fullName: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresAtEpochMillis: Long,
)
