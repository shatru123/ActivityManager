package com.lifelogger.ai.core.database.di

import android.content.Context
import androidx.room.Room
import com.lifelogger.ai.core.database.LifeLoggerDatabase
import com.lifelogger.ai.core.database.dao.ActivityLogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): LifeLoggerDatabase = Room.databaseBuilder(
        context,
        LifeLoggerDatabase::class.java,
        "lifelogger.db",
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideActivityLogDao(
        database: LifeLoggerDatabase,
    ): ActivityLogDao = database.activityLogDao()
}
