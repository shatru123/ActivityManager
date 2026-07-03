package com.lifelogger.ai.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelogger.ai.core.domain.usecase.LoginUseCase
import com.lifelogger.ai.core.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {
    private val mutableState = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = mutableState.asStateFlow()

    fun onModeChanged(mode: AuthMode) {
        mutableState.update {
            it.copy(
                mode = mode,
                errorMessage = null,
            )
        }
    }

    fun onFullNameChanged(value: String) {
        mutableState.update { it.copy(fullName = value) }
    }

    fun onEmailChanged(value: String) {
        mutableState.update { it.copy(email = value) }
    }

    fun onPasswordChanged(value: String) {
        mutableState.update { it.copy(password = value) }
    }

    fun onConfirmPasswordChanged(value: String) {
        mutableState.update { it.copy(confirmPassword = value) }
    }

    fun submit() {
        val currentState = state.value
        val validationError = validate(currentState)
        if (validationError != null) {
            mutableState.update { it.copy(errorMessage = validationError) }
            return
        }

        viewModelScope.launch {
            mutableState.update { it.copy(isSubmitting = true, errorMessage = null) }

            val result = if (currentState.mode == AuthMode.LOGIN) {
                loginUseCase(currentState.email, currentState.password)
            } else {
                registerUseCase(
                    currentState.fullName,
                    currentState.email,
                    currentState.password,
                )
            }

            mutableState.update {
                it.copy(
                    isSubmitting = false,
                    errorMessage = result.exceptionOrNull()?.message,
                )
            }
        }
    }

    private fun validate(currentState: AuthUiState): String? {
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            return "Email and password are required."
        }

        if (currentState.mode == AuthMode.REGISTER) {
            if (currentState.fullName.isBlank()) {
                return "Full name is required."
            }

            if (currentState.password != currentState.confirmPassword) {
                return "Passwords do not match."
            }
        }

        return null
    }
}
