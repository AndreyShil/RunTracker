package my.training.core.core_impl.di.modules

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import my.training.core.core_api.di.qualifiers.DispatcherIO
import my.training.core.core_impl.data.network.AuthInterceptor
import my.training.core.core_impl.data.network.UserApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
internal object NetworkModule {

    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addLoggingInterceptor()
            .addInterceptor(AuthInterceptor())
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .baseUrl(" https://f324-5-164-212-43.ngrok-free.app")
            .build()
    }

    @Provides
    fun provideUserApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @DispatcherIO
    fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

    private fun OkHttpClient.Builder.addLoggingInterceptor(): OkHttpClient.Builder {
        return addInterceptor(
            HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        )
    }
}