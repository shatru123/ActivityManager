package com.lifelogger.ai.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.appPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "app_preferences",
)

interface AppPreferences {
    val lastUsageImportAtEpochMillis: Flow<Long?>

    suspend fun updateLastUsageImportAt(epochMillis: Long)
}

@Singleton
class DataStoreAppPreferences @Inject constructor(
    @ApplicationContext private val context: Context,
) : AppPreferences {
    override val lastUsageImportAtEpochMillis: Flow<Long?> =
        context.appPreferencesDataStore.data.map { preferences ->
            preferences[LAST_USAGE_IMPORT_AT]
        }

    override suspend fun updateLastUsageImportAt(epochMillis: Long) {
        context.appPreferencesDataStore.edit { preferences ->
            preferences[LAST_USAGE_IMPORT_AT] = epochMillis
        }
    }

    private companion object {
        val LAST_USAGE_IMPORT_AT = longPreferencesKey("last_usage_import_at")
    }
}
