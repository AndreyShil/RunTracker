package my.training.core.core_impl.di.components

import dagger.Component
import my.training.core.core_api.di.ContextProvider
import my.training.core.core_api.di.PreferencesProvider
import my.training.core.core_api.di.scopes.PerApplication
import my.training.core.core_impl.di.modules.PreferencesModule

@Component(
    modules = [PreferencesModule::class],
    dependencies = [ContextProvider::class]
)
@PerApplication
interface PreferencesComponent : PreferencesProvider {

    companion object {
        fun create(contextProvider: ContextProvider): PreferencesComponent {
            return DaggerPreferencesComponent.builder()
                .contextProvider(contextProvider)
                .build()
        }
    }

}