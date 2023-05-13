package my.training.feature.auth.di

import dagger.Component
import my.training.core.core_api.di.AggregatingProvider
import my.training.core.core_api.di.scopes.PerFragment
import my.training.feature.auth.presentation.sign_in.SignInFragment

@Component(
    modules = [AuthModule::class],
    dependencies = [AggregatingProvider::class]
)
@PerFragment
internal interface SignInComponent {

    fun inject(fragment: SignInFragment)

    companion object {
        fun create(aggregatingProvider: AggregatingProvider): SignInComponent {
            return DaggerSignInComponent.builder()
                .aggregatingProvider(aggregatingProvider)
                .build()
        }
    }
}