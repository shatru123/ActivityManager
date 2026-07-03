package com.lifelogger.ai.feature.auth

enum class AuthMode {
    LOGIN,
    REGISTER,
}

data class AuthUiState(
    val mode: AuthMode = AuthMode.LOGIN,
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
)
