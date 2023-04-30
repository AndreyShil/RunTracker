package com.example.core_provider

import my.training.core.core_api.di.UserRepositoryProvider
import my.training.core.core_impl.di.components.UserRepositoryComponent

object CoreProviderFactory {

    fun createUserRepositoryProvider(): UserRepositoryProvider {
        return UserRepositoryComponent.create()
    }
}