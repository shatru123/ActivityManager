package com.lifelogger.ai.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelogger.ai.core.domain.usecase.ObserveSessionUseCase
import com.lifelogger.ai.core.model.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class RootViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
) : ViewModel() {
    val session: StateFlow<UserSession?> = observeSessionUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
}
