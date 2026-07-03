package com.lifelogger.ai.core.domain.repository

import com.lifelogger.ai.core.model.UserSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeSession(): Flow<UserSession?>

    suspend fun login(
        email: String,
        password: String,
    ): Result<Unit>

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
    ): Result<Unit>

    suspend fun refreshSession(): Result<Unit>

    suspend fun logout()
}
