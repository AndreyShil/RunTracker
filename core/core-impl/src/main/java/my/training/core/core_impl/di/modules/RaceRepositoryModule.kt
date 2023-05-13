package my.training.core.core_impl.di.modules

import dagger.Binds
import dagger.Module
import my.training.core.core_api.domain.repository.RaceRepository
import my.training.core.core_impl.data.repository.RaceRepositoryImpl

@Module
internal interface RaceRepositoryModule {

    @Binds
    fun bindRaceRepository(impl: RaceRepositoryImpl): RaceRepository

}