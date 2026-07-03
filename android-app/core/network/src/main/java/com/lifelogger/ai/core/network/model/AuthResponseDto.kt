package com.lifelogger.ai.core.network.model

data class AuthResponseDto(
    val userId: String,
    val fullName: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresAtUtc: String,
)
