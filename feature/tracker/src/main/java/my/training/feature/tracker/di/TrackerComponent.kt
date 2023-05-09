package my.training.feature.tracker.di

import dagger.Component
import my.training.core.core_api.di.ProvidersFacade
import my.training.core.core_api.di.scopes.PerFragment
import my.training.feature.tracker.presentation.tracker.TrackerFragment

@Component(
    dependencies = [ProvidersFacade::class]
)
@PerFragment
internal interface TrackerComponent {

    fun inject(fragment: TrackerFragment)

    companion object {
        fun create(providersFacade: ProvidersFacade): TrackerComponent {
            return DaggerTrackerComponent.builder()
                .providersFacade(providersFacade)
                .build()
        }
    }
}