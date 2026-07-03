package com.lifelogger.ai.core.data.di

import com.lifelogger.ai.core.data.repository.DefaultAuthRepository
import com.lifelogger.ai.core.data.repository.DefaultTimelineRepository
import com.lifelogger.ai.core.domain.repository.AuthRepository
import com.lifelogger.ai.core.domain.repository.TimelineRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        implementation: DefaultAuthRepository,
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTimelineRepository(
        implementation: DefaultTimelineRepository,
    ): TimelineRepository
}
