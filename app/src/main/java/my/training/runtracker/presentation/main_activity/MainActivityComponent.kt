package my.training.runtracker.presentation.main_activity

import dagger.Component
import my.training.core.core_api.di.AggregatingProvider
import my.training.core.core_api.di.scopes.PerActivity

@Component(
    dependencies = [AggregatingProvider::class]
)
@PerActivity
internal interface MainActivityComponent {

    fun inject(activity: MainActivity)

    companion object {
        fun create(aggregatingProvider: AggregatingProvider): MainActivityComponent {
            return DaggerMainActivityComponent.builder()
                .aggregatingProvider(aggregatingProvider)
                .build()
        }
    }
}