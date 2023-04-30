package my.training.core.core_impl.data.network

import okhttp3.Interceptor
import okhttp3.Response

internal class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
            .header("Authorization", "2768c8e8-063a-47fc-8f1d-1b415ea0901e")
            .build()
        return chain.proceed(requestBuilder)
    }

}