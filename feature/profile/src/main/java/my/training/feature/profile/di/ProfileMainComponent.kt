package my.training.feature.profile.di

import dagger.Component
import my.training.core.core_api.di.AggregatingProvider
import my.training.core.core_api.di.scopes.PerFragment
import my.training.feature.profile.presentation.main.ProfileFragment

@Component(
    modules = [ProfileModule::class],
    dependencies = [AggregatingProvider::class]
)
@PerFragment
internal interface ProfileMainComponent {

    fun inject(fragment: ProfileFragment)

    companion object {
        fun create(aggregatingProvider: AggregatingProvider): ProfileMainComponent {
            return DaggerProfileMainComponent.builder()
                .aggregatingProvider(aggregatingProvider)
                .build()
        }
    }
}