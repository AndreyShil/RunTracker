package my.training.feature.auth.di

import dagger.Module
import dagger.Provides
import my.training.core.core_api.di.HomeMediator
import javax.inject.Provider

@Module
internal object AuthModule {

    @Provides
    fun provideHomeMediator(map: Map<Class<*>, @JvmSuppressWildcards Provider<Any>>): HomeMediator {
        return map[HomeMediator::class.java]?.get() as HomeMediator
    }

}