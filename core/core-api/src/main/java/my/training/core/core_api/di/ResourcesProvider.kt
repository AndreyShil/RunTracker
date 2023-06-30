package my.training.core.core_api.di

import my.training.core.core_api.domain.manager.ResourcesManager

interface ResourcesProvider {

    fun provideResourcesManager(): ResourcesManager
}