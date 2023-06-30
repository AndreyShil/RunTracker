package my.training.feature.auth.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import my.training.core.core_api.di.HomeMediator
import my.training.feature.auth.data.AuthRepositoryImpl
import my.training.feature.auth.data.AuthRepository
import javax.inject.Provider

@Module
internal interface AuthModule {

    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    companion object {
        @Provides
        fun provideHomeMediator(map: Map<Class<*>, @JvmSuppressWildcards Provider<Any>>): HomeMediator {
            return map[HomeMediator::class.java]?.get() as HomeMediator
        }
    }
}