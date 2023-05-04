package my.training.feature.profile.di

import dagger.Component
import my.training.core.core_api.di.ProvidersFacade
import my.training.core.core_api.di.scopes.PerFragment
import my.training.feature.profile.presentation.main.ProfileFragment

@Component(
    dependencies = [ProvidersFacade::class]
)
@PerFragment
internal interface ProfileMainComponent {

    fun inject(fragment: ProfileFragment)

    companion object {
        fun create(providersFacade: ProvidersFacade): ProfileMainComponent {
            return DaggerProfileMainComponent.builder()
                .providersFacade(providersFacade)
                .build()
        }
    }
}