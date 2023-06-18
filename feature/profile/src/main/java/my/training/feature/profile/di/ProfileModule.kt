package my.training.feature.profile.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import my.training.core.core_api.di.HomeMediator
import my.training.feature.profile.data.ProfileRepositoryImpl
import my.training.feature.profile.domain.ProfileRepository
import javax.inject.Provider

@Module
internal interface ProfileModule {

    @Binds
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    companion object {
        @Provides
        fun provideHomeMediator(map: Map<Class<*>, @JvmSuppressWildcards Provider<Any>>): HomeMediator {
            return map[HomeMediator::class.java]?.get() as HomeMediator
        }
    }
}