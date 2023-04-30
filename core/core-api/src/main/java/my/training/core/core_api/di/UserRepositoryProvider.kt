package my.training.core.core_api.di

import my.training.core.core_api.domain.repository.UserRepository

interface UserRepositoryProvider {

    fun provideUserRepository(): UserRepository
}