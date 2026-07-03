package com.lifelogger.ai.feature.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifelogger.ai.core.domain.usecase.HasUsagePermissionUseCase
import com.lifelogger.ai.core.domain.usecase.LogoutUseCase
import com.lifelogger.ai.core.domain.usecase.ObserveSessionUseCase
import com.lifelogger.ai.core.domain.usecase.ObserveTimelineUseCase
import com.lifelogger.ai.core.domain.usecase.RefreshTimelineUseCase
import com.lifelogger.ai.core.model.ActivityLog
import com.lifelogger.ai.core.model.AppUsageStat
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Duration
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TimelineViewModel @Inject constructor(
    observeTimelineUseCase: ObserveTimelineUseCase,
    observeSessionUseCase: ObserveSessionUseCase,
    private val refreshTimelineUseCase: RefreshTimelineUseCase,
    private val hasUsagePermissionUseCase: HasUsagePermissionUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val mutableState = MutableStateFlow(TimelineUiState())
    val state: StateFlow<TimelineUiState> = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            observeSessionUseCase().collect { session ->
                mutableState.update {
                    it.copy(userName = session?.fullName.orEmpty())
                }
            }
        }

        viewModelScope.launch {
            observeTimelineUseCase(state.value.selectedDate).collect { entries ->
                mutableState.update {
                    it.copy(
                        entries = entries,
                        topApps = entries.toTopApps(),
                        totalTrackedMinutes = entries.sumOf { entry ->
                            entry.duration.toMinutes()
                        },
                    )
                }
            }
        }

        syncPermissionState()
        refresh()
    }

    fun onResume() {
        syncPermissionState()
        refresh()
    }

    fun refresh() {
        if (!hasUsagePermissionUseCase()) {
            mutableState.update {
                it.copy(
                    hasUsagePermission = false,
                    isRefreshing = false,
                    errorMessage = null,
                )
            }
            return
        }

        viewModelScope.launch {
            mutableState.update { it.copy(isRefreshing = true, errorMessage = null) }
            val result = refreshTimelineUseCase(state.value.selectedDate)
            mutableState.update {
                it.copy(
                    isRefreshing = false,
                    errorMessage = result.exceptionOrNull()?.message,
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    private fun syncPermissionState() {
        mutableState.update {
            it.copy(hasUsagePermission = hasUsagePermissionUseCase())
        }
    }
}

private fun List<ActivityLog>.toTopApps(): List<AppUsageStat> =
    groupBy { it.appName }
        .map { (appName, logs) ->
            AppUsageStat(
                appName = appName,
                duration = logs.fold(Duration.ZERO) { acc, log -> acc.plus(log.duration) },
            )
        }
        .sortedByDescending { it.duration }
        .take(3)
