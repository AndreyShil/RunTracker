package my.training.feature.tracker.di

import dagger.Component
import my.training.core.core_api.di.ProvidersFacade
import my.training.core.core_api.di.scopes.PerFragment
import my.training.feature.tracker.presentation.races.RacesFragment

@Component(
    dependencies = [ProvidersFacade::class]
)
@PerFragment
internal interface RacesComponent {

    fun inject(fragment: RacesFragment)

    companion object {
        fun create(providersFacade: ProvidersFacade): RacesComponent {
            return DaggerRacesComponent.builder()
                .providersFacade(providersFacade)
                .build()
        }
    }
}