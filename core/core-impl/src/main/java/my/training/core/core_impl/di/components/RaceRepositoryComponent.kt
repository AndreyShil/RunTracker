package my.training.core.core_impl.di.components

import dagger.Component
import my.training.core.core_api.di.ContextProvider
import my.training.core.core_api.di.RaceRepositoryProvider
import my.training.core.core_api.di.scopes.PerApplication
import my.training.core.core_impl.di.modules.NetworkModule
import my.training.core.core_impl.di.modules.RaceRepositoryModule

@Component(
    modules = [
        RaceRepositoryModule::class,
        NetworkModule::class
    ],
    dependencies = [ContextProvider::class]
)
@PerApplication
interface RaceRepositoryComponent : RaceRepositoryProvider {

    companion object {
        fun create(contextProvider: ContextProvider): RaceRepositoryComponent {
            return DaggerRaceRepositoryComponent.builder()
                .contextProvider(contextProvider)
                .build()
        }
    }
}