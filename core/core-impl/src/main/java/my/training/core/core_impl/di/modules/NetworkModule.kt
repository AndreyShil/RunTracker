package my.training.core.core_impl.di.modules

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import my.training.core.core_api.di.qualifiers.DispatcherIO
import my.training.core.core_impl.data.network.RemoteDataSource
import my.training.core.core_impl.data.network.UserApiService

@Module(
    includes = [PreferencesModule::class]
)
internal object NetworkModule {

    @Provides
    fun provideUserApiService(remoteDataSource: RemoteDataSource): UserApiService {
        return remoteDataSource.buildApi(UserApiService::class.java)
    }

    @Provides
    @DispatcherIO
    fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO
}