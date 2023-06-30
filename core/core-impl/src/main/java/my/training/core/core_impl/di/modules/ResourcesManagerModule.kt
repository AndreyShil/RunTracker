package my.training.core.core_impl.di.modules

import dagger.Binds
import dagger.Module
import my.training.core.core_api.domain.manager.ResourcesManager
import my.training.core.core_impl.data.manager.ResourcesManagerImpl

@Module
internal interface ResourcesManagerModule {

    @Binds
    fun bindResourcesManager(impl: ResourcesManagerImpl): ResourcesManager
}