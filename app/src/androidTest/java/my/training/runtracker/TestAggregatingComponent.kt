package my.training.runtracker

import android.content.Context
import androidx.test.espresso.idling.CountingIdlingResource
import com.example.core_provider.CoreProviderFactory
import dagger.BindsOptionalOf
import dagger.Component
import dagger.Module
import dagger.Provides
import my.training.core.core_api.di.AggregatingProvider
import my.training.core.core_api.di.ContextProvider
import my.training.core.core_api.di.DatabaseProvider
import my.training.core.core_api.di.FirebaseStorageProvider
import my.training.core.core_api.di.NetworkProvider
import my.training.core.core_api.di.PreferencesProvider
import my.training.core.core_api.di.ResourcesProvider
import my.training.core.core_api.di.UserRepositoryProvider
import my.training.runtracker.di.ContextComponent
import my.training.runtracker.di.HomeMediatorModule
import javax.inject.Singleton

@Component(
    modules = [HomeMediatorModule::class, TestIdlingResourceModule::class],
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
@Singleton
interface TestAggregatingComponent : AggregatingProvider {

    fun inject(userFlowTest: UserAuthTest)

    companion object {

        private var aggregatingComponent: TestAggregatingComponent? = null

        fun create(context: Context): TestAggregatingComponent {
            val contextProvider = ContextComponent.create(context)
            return aggregatingComponent ?: DaggerTestAggregatingComponent.factory()
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
        ): TestAggregatingComponent
    }

}

@Module
interface TestIdlingResourceModule {

    companion object {
        @Provides
        @Singleton
        fun provideIdlingResource(): CountingIdlingResource {
            return CountingIdlingResource("test_resource")
        }
    }

    @BindsOptionalOf
    fun provideIdlingResource(): CountingIdlingResource
}

