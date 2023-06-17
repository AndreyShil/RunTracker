package my.training.feature.stats.di

import dagger.Component
import my.training.core.core_api.di.AggregatingProvider
import my.training.feature.stats.presentation.stats_screen.StatsFragment

@Component(
    modules = [StatsModule::class],
    dependencies = [AggregatingProvider::class]
)
internal interface StatsComponent {

    fun inject(fragment: StatsFragment)

    companion object {
        fun create(aggregatingProvider: AggregatingProvider): StatsComponent {
            return DaggerStatsComponent.builder()
                .aggregatingProvider(aggregatingProvider)
                .build()
        }
    }
}