package my.training.core.core_impl.data.network

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import my.training.core.core_api.di.qualifiers.AppContext
import my.training.core.core_api.domain.preferences.Preferences
import my.training.core.core_impl.R
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Inject

private const val CONTENT_TYPE = "application/json"

class RemoteDataSource @Inject constructor(
    @AppContext private val context: Context,
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
        val contentType = CONTENT_TYPE.toMediaType()
        return Retrofit.Builder()
            .client(getOkhttpClient())
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl("http://${context.getString(R.string.localhost)}")
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
