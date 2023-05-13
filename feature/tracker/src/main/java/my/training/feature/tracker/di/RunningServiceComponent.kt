package my.training.feature.tracker.di

import dagger.Component
import my.training.core.core_api.di.AggregatingProvider
import my.training.core.core_api.di.scopes.PerService
import my.training.feature.tracker.domain.service.RunningService

@Component(
    modules = [LocationClientModule::class],
    dependencies = [AggregatingProvider::class]
)
@PerService
internal interface RunningServiceComponent {

    fun inject(service: RunningService)

    companion object {
        fun create(aggregatingProvider: AggregatingProvider): RunningServiceComponent {
            return DaggerRunningServiceComponent.builder()
                .aggregatingProvider(aggregatingProvider)
                .build()
        }
    }
}