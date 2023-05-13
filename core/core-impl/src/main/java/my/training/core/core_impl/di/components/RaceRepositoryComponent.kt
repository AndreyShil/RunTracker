package my.training.core.core_impl.di.components

import dagger.Component
import my.training.core.core_api.di.RaceRepositoryProvider
import my.training.core.core_api.di.scopes.PerApplication
import my.training.core.core_impl.di.modules.RaceRepositoryModule

@Component(
    modules = [
        RaceRepositoryModule::class
    ]
)
@PerApplication
interface RaceRepositoryComponent : RaceRepositoryProvider {

    companion object {
        fun create(): RaceRepositoryComponent {
            return DaggerRaceRepositoryComponent.builder().build()
        }
    }
}