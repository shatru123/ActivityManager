package com.lifelogger.ai.core.datastore

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.lifelogger.ai.core.model.UserSession
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface SecureSessionStorage {
    fun read(): UserSession?

    fun save(session: UserSession)

    fun clear()
}

@Singleton
class EncryptedSessionStorage @Inject constructor(
    @ApplicationContext context: Context,
) : SecureSessionStorage {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val preferences = EncryptedSharedPreferences.create(
        context,
        "secure_session",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    override fun read(): UserSession? {
        val accessToken = preferences.getString(KEY_ACCESS_TOKEN, null) ?: return null
        val refreshToken = preferences.getString(KEY_REFRESH_TOKEN, null) ?: return null
        val userId = preferences.getString(KEY_USER_ID, null) ?: return null
        val fullName = preferences.getString(KEY_FULL_NAME, null) ?: return null
        val email = preferences.getString(KEY_EMAIL, null) ?: return null
        val expiresAt = preferences.getLong(KEY_EXPIRES_AT, 0L)

        return UserSession(
            userId = userId,
            fullName = fullName,
            email = email,
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresAtEpochMillis = expiresAt,
        )
    }

    override fun save(session: UserSession) {
        preferences.edit(commit = true) {
            putString(KEY_USER_ID, session.userId)
            putString(KEY_FULL_NAME, session.fullName)
            putString(KEY_EMAIL, session.email)
            putString(KEY_ACCESS_TOKEN, session.accessToken)
            putString(KEY_REFRESH_TOKEN, session.refreshToken)
            putLong(KEY_EXPIRES_AT, session.accessTokenExpiresAtEpochMillis)
        }
    }

    override fun clear() {
        preferences.edit(commit = true) { clear() }
    }

    private companion object {
        const val KEY_USER_ID = "key_user_id"
        const val KEY_FULL_NAME = "key_full_name"
        const val KEY_EMAIL = "key_email"
        const val KEY_ACCESS_TOKEN = "key_access_token"
        const val KEY_REFRESH_TOKEN = "key_refresh_token"
        const val KEY_EXPIRES_AT = "key_expires_at"
    }
}
