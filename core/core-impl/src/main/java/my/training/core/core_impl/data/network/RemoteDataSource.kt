package my.training.core.core_impl.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import my.training.core.core_api.domain.preferences.Preferences
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Inject

internal class RemoteDataSource @Inject constructor(
    private val preferences: Preferences
) {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun <Api> buildApi(
        api: Class<Api>,
    ): Api {
        return getRetrofit().create(api)
    }

    private fun getRetrofit(): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .client(getOkhttpClient())
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl("https://fca2-5-164-206-99.ngrok-free.app")
//            .baseUrl("http://192.168.0.64:8080")
            .build()
    }

    private fun getOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addLoggingInterceptor()
            .addInterceptor(AuthInterceptor(preferences))
            .build()
    }

    private fun OkHttpClient.Builder.addLoggingInterceptor(): OkHttpClient.Builder {
        return addInterceptor(
            HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        )
    }

}