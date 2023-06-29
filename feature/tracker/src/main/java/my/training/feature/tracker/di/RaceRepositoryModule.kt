package my.training.feature.tracker.di

import dagger.Binds
import dagger.Module
import my.training.feature.tracker.data.repository.RaceRepository
import my.training.feature.tracker.data.repository.RaceRepositoryImpl

@Module
internal interface RaceRepositoryModule {

    @Binds
    fun bindRaceRepository(impl: RaceRepositoryImpl): RaceRepository
}