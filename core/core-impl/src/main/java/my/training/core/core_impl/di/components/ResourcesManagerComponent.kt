package my.training.core.core_impl.di.components

import dagger.Component
import my.training.core.core_api.di.ContextProvider
import my.training.core.core_api.di.ResourcesProvider
import my.training.core.core_impl.di.modules.ResourcesManagerModule

@Component(
    modules = [ResourcesManagerModule::class],
    dependencies = [ContextProvider::class]
)
interface ResourcesManagerComponent : ResourcesProvider {

    companion object {
        fun create(contextProvider: ContextProvider): ResourcesManagerComponent {
            return DaggerResourcesManagerComponent.builder()
                .contextProvider(contextProvider)
                .build()
        }
    }
}