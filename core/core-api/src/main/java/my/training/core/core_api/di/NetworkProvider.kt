package my.training.core.core_api.di

import kotlinx.coroutines.CoroutineDispatcher
import my.training.core.core_api.data.network.AuthApiService
import my.training.core.core_api.data.network.RacesApiService
import my.training.core.core_api.data.network.UserApiService
import my.training.core.core_api.di.qualifiers.DispatcherIO

interface NetworkProvider {

    @DispatcherIO
    fun provideIDispatcherIO(): CoroutineDispatcher

    fun provideAuthApiService(): AuthApiService
    fun provideUserApiService(): UserApiService
    fun providerRacesApiService(): RacesApiService
}