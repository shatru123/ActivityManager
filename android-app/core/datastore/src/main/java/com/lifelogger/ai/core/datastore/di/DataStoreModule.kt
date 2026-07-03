package com.lifelogger.ai.core.datastore.di

import com.lifelogger.ai.core.datastore.AppPreferences
import com.lifelogger.ai.core.datastore.DataStoreAppPreferences
import com.lifelogger.ai.core.datastore.EncryptedSessionStorage
import com.lifelogger.ai.core.datastore.SecureSessionStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {
    @Binds
    @Singleton
    abstract fun bindSecureSessionStorage(
        implementation: EncryptedSessionStorage,
    ): SecureSessionStorage

    @Binds
    @Singleton
    abstract fun bindAppPreferences(
        implementation: DataStoreAppPreferences,
    ): AppPreferences
}
