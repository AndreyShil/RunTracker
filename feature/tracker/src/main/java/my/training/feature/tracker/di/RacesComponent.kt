package my.training.feature.tracker.di

import dagger.Component
import my.training.core.core_api.di.AggregatingProvider
import my.training.core.core_api.di.scopes.PerFragment
import my.training.feature.tracker.presentation.races.RacesFragment

@Component(
    modules = [RaceRepositoryModule::class],
    dependencies = [AggregatingProvider::class]
)
@PerFragment
internal interface RacesComponent {

    fun inject(fragment: RacesFragment)

    companion object {
        fun create(aggregatingProvider: AggregatingProvider): RacesComponent {
            return DaggerRacesComponent.builder()
                .aggregatingProvider(aggregatingProvider)
                .build()
        }
    }
}