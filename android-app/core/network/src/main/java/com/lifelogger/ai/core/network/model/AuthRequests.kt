package com.lifelogger.ai.core.network.model

data class RegisterRequestDto(
    val fullName: String,
    val email: String,
    val password: String,
)

data class LoginRequestDto(
    val email: String,
    val password: String,
)

data class RefreshTokenRequestDto(
    val refreshToken: String,
)
