package com.lifelogger.ai.feature.timeline

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lifelogger.ai.core.common.util.toReadableDuration
import com.lifelogger.ai.core.common.util.toTimelineTime
import com.lifelogger.ai.core.ui.components.StatCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineRoute(
    viewModel: TimelineViewModel = hiltViewModel(),
) {
    val uiState = viewModel.state.collectAsStateWithLifecycle().value

    LifecycleResumeEffect(Unit) {
        viewModel.onResume()
        onPauseOrDispose { }
    }

    TimelineScreen(
        state = uiState,
        onRefresh = viewModel::refresh,
        onLogout = viewModel::logout,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimelineScreen(
    state: TimelineUiState,
    onRefresh: () -> Unit,
    onLogout: () -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Timeline") },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Outlined.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Outlined.Logout, contentDescription = "Logout")
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                StatCard(
                    title = "Hello ${state.userName.ifBlank { "there" }}",
                    value = "${state.totalTrackedMinutes} tracked minutes",
                    supportingText = "Phase 1 tracks foreground app usage locally and refreshes throughout the day.",
                )
            }

            if (state.topApps.isNotEmpty()) {
                item {
                    Card {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = "Most used apps",
                                style = MaterialTheme.typography.titleMedium,
                            )
                            state.topApps.forEach { stat ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(stat.appName)
                                    Text(stat.duration.toReadableDuration())
                                }
                            }
                        }
                    }
                }
            }

            if (!state.hasUsagePermission) {
                item {
                    Card {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Text(
                                text = "Usage access required",
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = "LifeLogger AI only records app metadata after you explicitly grant Usage Access in Android settings.",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Button(
                                onClick = {
                                    context.startActivity(
                                        Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        },
                                    )
                                },
                            ) {
                                Text("Grant access")
                            }
                        }
                    }
                }
            } else {
                if (state.isRefreshing) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                if (state.entries.isEmpty()) {
                    item {
                        Text(
                            text = "No tracked activity yet for today.",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                } else {
                    items(state.entries, key = { it.id }) { entry ->
                        Card {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                Text(
                                    text = entry.appName,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Text(
                                    text = entry.packageName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Text(
                                    text = "${entry.startTime.toTimelineTime()} - ${entry.endTime.toTimelineTime()}",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                Text(
                                    text = entry.duration.toReadableDuration(),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }
            }

            state.errorMessage?.let { message ->
                item {
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}
