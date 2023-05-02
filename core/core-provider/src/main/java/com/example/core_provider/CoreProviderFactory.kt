package com.example.core_provider

import my.training.core.core_api.di.ContextProvider
import my.training.core.core_api.di.DatabaseProvider
import my.training.core.core_api.di.PreferencesProvider
import my.training.core.core_api.di.UserRepositoryProvider
import my.training.core.core_impl.di.components.DatabaseComponent
import my.training.core.core_impl.di.components.PreferencesComponent
import my.training.core.core_impl.di.components.UserRepositoryComponent

object CoreProviderFactory {

    fun createUserRepositoryProvider(contextProvider: ContextProvider): UserRepositoryProvider {
        return UserRepositoryComponent.create(contextProvider)
    }

    fun createPreferencesProvider(contextProvider: ContextProvider): PreferencesProvider {
        return PreferencesComponent.create(contextProvider)
    }

    fun createDatabaseProvider(contextProvider: ContextProvider): DatabaseProvider {
        return DatabaseComponent.create(contextProvider)
    }
}