package my.training.feature.tracker.di

import dagger.Binds
import dagger.Module
import my.training.feature.tracker.data.RaceRepositoryImpl
import my.training.feature.tracker.domain.RaceRepository

@Module
internal interface RaceRepositoryModule {

    @Binds
    fun bindRaceRepository(impl: RaceRepositoryImpl): RaceRepository

}