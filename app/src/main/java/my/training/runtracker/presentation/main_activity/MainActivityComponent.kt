package my.training.runtracker.presentation.main_activity

import dagger.Component
import my.training.core.core_api.di.ProvidersFacade
import my.training.core.core_api.di.scopes.PerActivity

@Component(
    dependencies = [ProvidersFacade::class]
)
@PerActivity
internal interface MainActivityComponent {

    fun inject(activity: MainActivity)

    companion object {
        fun create(providersFacade: ProvidersFacade): MainActivityComponent {
            return DaggerMainActivityComponent.builder()
                .providersFacade(providersFacade)
                .build()
        }
    }
}