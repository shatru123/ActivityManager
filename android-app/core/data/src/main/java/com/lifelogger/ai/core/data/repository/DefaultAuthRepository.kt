package com.lifelogger.ai.core.data.repository

import com.lifelogger.ai.core.datastore.SecureSessionStorage
import com.lifelogger.ai.core.domain.repository.AuthRepository
import com.lifelogger.ai.core.network.api.AuthApiService
import com.lifelogger.ai.core.network.model.LoginRequestDto
import com.lifelogger.ai.core.network.model.RefreshTokenRequestDto
import com.lifelogger.ai.core.network.model.RegisterRequestDto
import com.lifelogger.ai.core.model.UserSession
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class DefaultAuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val secureSessionStorage: SecureSessionStorage,
) : AuthRepository {
    private val sessionState = MutableStateFlow(secureSessionStorage.read())

    override fun observeSession(): Flow<UserSession?> = sessionState.asStateFlow()

    override suspend fun login(
        email: String,
        password: String,
    ): Result<Unit> = runCatching {
        val response = authApiService.login(
            LoginRequestDto(
                email = email.trim(),
                password = password,
            ),
        )

        val session = response.asSession()
        secureSessionStorage.save(session)
        sessionState.value = session
    }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
    ): Result<Unit> = runCatching {
        val response = authApiService.register(
            RegisterRequestDto(
                fullName = fullName.trim(),
                email = email.trim(),
                password = password,
            ),
        )

        val session = response.asSession()
        secureSessionStorage.save(session)
        sessionState.value = session
    }

    override suspend fun refreshSession(): Result<Unit> = runCatching {
        val currentSession = sessionState.value ?: error("No active session.")
        val response = authApiService.refresh(
            RefreshTokenRequestDto(
                refreshToken = currentSession.refreshToken,
            ),
        )

        val updatedSession = response.asSession()
        secureSessionStorage.save(updatedSession)
        sessionState.value = updatedSession
    }

    override suspend fun logout() {
        secureSessionStorage.clear()
        sessionState.value = null
    }
}
