package my.training.feature.auth.di

import dagger.Component
import my.training.core.core_api.di.AggregatingProvider
import my.training.core.core_api.di.scopes.PerFragment
import my.training.feature.auth.presentation.sign_up.SignUpFragment

@Component(
    modules = [AuthModule::class],
    dependencies = [AggregatingProvider::class]
)
@PerFragment
internal interface SignUpComponent {

    fun inject(fragment: SignUpFragment)

    companion object {
        fun create(aggregatingProvider: AggregatingProvider): SignUpComponent {
            return DaggerSignUpComponent.builder()
                .aggregatingProvider(aggregatingProvider)
                .build()
        }
    }
}