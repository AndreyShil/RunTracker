package my.training.runtracker.di

import android.content.Context
import com.example.core_provider.CoreProviderFactory
import dagger.Component
import my.training.core.core_api.di.ContextProvider
import my.training.core.core_api.di.DatabaseProvider
import my.training.core.core_api.di.PreferencesProvider
import my.training.core.core_api.di.ProvidersFacade
import my.training.core.core_api.di.UserRepositoryProvider
import my.training.core.core_api.di.scopes.PerApplication

@Component(
    modules = [HomeMediatorModule::class],
    dependencies = [
        ContextProvider::class,
        UserRepositoryProvider::class,
        PreferencesProvider::class,
        DatabaseProvider::class
    ]
)
@PerApplication
interface FacadeComponent : ProvidersFacade {

    companion object {

        private var facadeComponent: FacadeComponent? = null

        fun create(context: Context): FacadeComponent {
            val contextProvider = ContextComponent.create(context)
            return facadeComponent ?: DaggerFacadeComponent.builder()
                .contextProvider(contextProvider)
                .userRepositoryProvider(
                    CoreProviderFactory.createUserRepositoryProvider(contextProvider)
                )
                .preferencesProvider(
                    CoreProviderFactory.createPreferencesProvider(contextProvider)
                )
                .databaseProvider(
                    CoreProviderFactory.createDatabaseProvider(contextProvider)
                )
                .build()
                .also { facadeComponent = it }
        }
    }

}