package com.lifelogger.ai.core.network.auth

import com.lifelogger.ai.core.datastore.SecureSessionStorage
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor @Inject constructor(
    private val secureSessionStorage: SecureSessionStorage,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val session = secureSessionStorage.read()
        val request = if (session?.accessToken.isNullOrBlank()) {
            chain.request()
        } else {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${session?.accessToken}")
                .build()
        }

        return chain.proceed(request)
    }
}
