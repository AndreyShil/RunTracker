package my.training.feature.auth.di

import dagger.Component
import my.training.core.core_api.ProvidersFacade
import my.training.core.core_api.scopes.PerFragment
import my.training.feature.auth.presentation.sign_up.SignUpFragment

@Component(
    modules = [AuthModule::class],
    dependencies = [ProvidersFacade::class]
)
@PerFragment
internal interface SignUpComponent {

    fun inject(fragment: SignUpFragment)

    companion object {
        fun create(providersFacade: ProvidersFacade): SignUpComponent {
            return DaggerSignUpComponent.builder()
                .providersFacade(providersFacade)
                .build()
        }
    }
}