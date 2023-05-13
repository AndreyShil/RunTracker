package my.training.feature.tracker.di

import dagger.Component
import my.training.core.core_api.di.AggregatingProvider
import my.training.core.core_api.di.scopes.PerFragment
import my.training.feature.tracker.presentation.tracker.TrackerFragment

@Component(
    dependencies = [AggregatingProvider::class]
)
@PerFragment
internal interface TrackerComponent {

    fun inject(fragment: TrackerFragment)

    companion object {
        fun create(aggregatingProvider: AggregatingProvider): TrackerComponent {
            return DaggerTrackerComponent.builder()
                .aggregatingProvider(aggregatingProvider)
                .build()
        }
    }
}