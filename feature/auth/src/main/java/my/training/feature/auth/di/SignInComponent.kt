package my.training.feature.auth.di

import dagger.Component
import my.training.core.core_api.di.ProvidersFacade
import my.training.core.core_api.di.scopes.PerFragment
import my.training.feature.auth.presentation.sign_in.SignInFragment

@Component(
    modules = [AuthModule::class],
    dependencies = [ProvidersFacade::class]
)
@PerFragment
internal interface SignInComponent {

    fun inject(fragment: SignInFragment)

    companion object {
        fun create(providersFacade: ProvidersFacade): SignInComponent {
            return DaggerSignInComponent.builder()
                .providersFacade(providersFacade)
                .build()
        }
    }
}