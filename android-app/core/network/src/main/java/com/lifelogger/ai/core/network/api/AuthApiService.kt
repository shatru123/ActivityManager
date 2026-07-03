package com.lifelogger.ai.core.network.api

import com.lifelogger.ai.core.network.model.AuthResponseDto
import com.lifelogger.ai.core.network.model.LoginRequestDto
import com.lifelogger.ai.core.network.model.RefreshTokenRequestDto
import com.lifelogger.ai.core.network.model.RegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequestDto,
    ): AuthResponseDto

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto,
    ): AuthResponseDto

    @POST("api/auth/refresh")
    suspend fun refresh(
        @Body request: RefreshTokenRequestDto,
    ): AuthResponseDto
}
