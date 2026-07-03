package com.lifelogger.ai.app.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lifelogger.ai.core.domain.usecase.HasUsagePermissionUseCase
import com.lifelogger.ai.core.domain.usecase.RefreshTimelineUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate

@HiltWorker
class UsageImportWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val hasUsagePermissionUseCase: HasUsagePermissionUseCase,
    private val refreshTimelineUseCase: RefreshTimelineUseCase,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        if (!hasUsagePermissionUseCase()) {
            return Result.success()
        }

        return refreshTimelineUseCase(LocalDate.now())
            .fold(
                onSuccess = { Result.success() },
                onFailure = { Result.retry() },
            )
    }

    companion object {
        const val WORK_NAME = "usage_import_worker"
    }
}
