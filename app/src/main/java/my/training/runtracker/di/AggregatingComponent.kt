package my.training.runtracker.di

import android.content.Context
import com.example.core_provider.CoreProviderFactory
import dagger.Component
import my.training.core.core_api.di.AggregatingProvider
import my.training.core.core_api.di.ContextProvider
import my.training.core.core_api.di.DatabaseProvider
import my.training.core.core_api.di.PreferencesProvider
import my.training.core.core_api.di.RaceRepositoryProvider
import my.training.core.core_api.di.UserRepositoryProvider
import my.training.core.core_api.di.scopes.PerApplication

@Component(
    modules = [HomeMediatorModule::class],
    dependencies = [
        ContextProvider::class,
        UserRepositoryProvider::class,
        RaceRepositoryProvider::class,
        PreferencesProvider::class,
        DatabaseProvider::class
    ]
)
@PerApplication
interface AggregatingComponent : AggregatingProvider {

    companion object {

        private var aggregatingComponent: AggregatingComponent? = null

        fun create(context: Context): AggregatingComponent {
            val contextProvider = ContextComponent.create(context)
            return aggregatingComponent ?: DaggerAggregatingComponent.factory()
                .create(
                    contextProvider = contextProvider,
                    userRepositoryProvider = CoreProviderFactory.userRepositoryProvider(
                        contextProvider
                    ),
                    raceRepositoryProvider = CoreProviderFactory.raceRepositoryProvider(),
                    preferencesProvider = CoreProviderFactory.preferencesProvider(contextProvider),
                    databaseProvider = CoreProviderFactory.databaseProvider(contextProvider)
                ).also { aggregatingComponent = it }
        }
    }

    @Component.Factory
    interface Factory {
        fun create(
            contextProvider: ContextProvider,
            userRepositoryProvider: UserRepositoryProvider,
            raceRepositoryProvider: RaceRepositoryProvider,
            preferencesProvider: PreferencesProvider,
            databaseProvider: DatabaseProvider
        ): AggregatingComponent
    }

}