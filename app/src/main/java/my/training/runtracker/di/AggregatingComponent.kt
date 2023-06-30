package my.training.runtracker.di

import android.content.Context
import com.example.core_provider.CoreProviderFactory
import dagger.Component
import my.training.core.core_api.di.AggregatingProvider
import my.training.core.core_api.di.ContextProvider
import my.training.core.core_api.di.DatabaseProvider
import my.training.core.core_api.di.FirebaseStorageProvider
import my.training.core.core_api.di.NetworkProvider
import my.training.core.core_api.di.PreferencesProvider
import my.training.core.core_api.di.ResourcesProvider
import my.training.core.core_api.di.UserRepositoryProvider
import my.training.core.core_api.di.scopes.PerApplication

@Component(
    modules = [HomeMediatorModule::class],
    dependencies = [
        ContextProvider::class,
        NetworkProvider::class,
        UserRepositoryProvider::class,
        PreferencesProvider::class,
        DatabaseProvider::class,
        FirebaseStorageProvider::class,
        ResourcesProvider::class
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
                    networkProvider = CoreProviderFactory.networkProvider(contextProvider),
                    preferencesProvider = CoreProviderFactory.preferencesProvider(contextProvider),
                    databaseProvider = CoreProviderFactory.databaseProvider(contextProvider),
                    firebaseStorageProvider = CoreProviderFactory.firebaseStorageProvider(),
                    resourcesManagerProvider = CoreProviderFactory.resourcesManagerProvider(
                        contextProvider
                    )
                ).also { aggregatingComponent = it }
        }
    }

    @Component.Factory
    interface Factory {
        fun create(
            contextProvider: ContextProvider,
            networkProvider: NetworkProvider,
            userRepositoryProvider: UserRepositoryProvider,
            preferencesProvider: PreferencesProvider,
            databaseProvider: DatabaseProvider,
            firebaseStorageProvider: FirebaseStorageProvider,
            resourcesManagerProvider: ResourcesProvider
        ): AggregatingComponent
    }
}
