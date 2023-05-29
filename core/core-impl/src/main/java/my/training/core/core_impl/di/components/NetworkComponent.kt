package my.training.core.core_impl.di.components

import dagger.Component
import my.training.core.core_api.di.ContextProvider
import my.training.core.core_api.di.NetworkProvider
import my.training.core.core_api.di.scopes.PerApplication
import my.training.core.core_impl.di.modules.NetworkModule

@Component(
    modules = [NetworkModule::class],
    dependencies = [ContextProvider::class]
)
@PerApplication
interface NetworkComponent : NetworkProvider {

    companion object {
        fun create(contextProvider: ContextProvider): NetworkComponent {
            return DaggerNetworkComponent.builder()
                .contextProvider(contextProvider)
                .build()
        }
    }
}