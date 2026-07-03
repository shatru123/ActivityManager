package com.lifelogger.ai.core.domain.usecase

import com.lifelogger.ai.core.domain.repository.AuthRepository
import com.lifelogger.ai.core.model.UserSession
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): Result<Unit> = authRepository.login(email, password)
}

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        fullName: String,
        email: String,
        password: String,
    ): Result<Unit> = authRepository.register(fullName, email, password)
}

class ObserveSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<UserSession?> = authRepository.observeSession()
}

class RefreshSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<Unit> = authRepository.refreshSession()
}

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke() = authRepository.logout()
}
