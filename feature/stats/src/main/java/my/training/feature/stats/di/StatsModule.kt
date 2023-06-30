package my.training.feature.stats.di

import dagger.Binds
import dagger.Module
import my.training.feature.stats.data.StatsRepositoryImpl
import my.training.feature.stats.data.StatsRepository

@Module
internal interface StatsModule {

    @Binds
    fun bindStatsRepository(impl: StatsRepositoryImpl): StatsRepository
}