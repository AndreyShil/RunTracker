package my.training.core.core_api.di

import my.training.core.core_api.domain.repository.RaceRepository

interface RaceRepositoryProvider {

    fun provideRaceRepository(): RaceRepository
}