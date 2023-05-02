package my.training.core.core_impl.data.network

import my.training.core.core_api.domain.preferences.Preferences
import okhttp3.Interceptor
import okhttp3.Response

internal class AuthInterceptor(
    private val preferences: Preferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        val accessToken = preferences.getAccessToken()
        if (!accessToken.isNullOrEmpty()) {
            requestBuilder.header(AUTHORIZATION, accessToken)
        }
        return chain.proceed(requestBuilder.build())
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
    }
}