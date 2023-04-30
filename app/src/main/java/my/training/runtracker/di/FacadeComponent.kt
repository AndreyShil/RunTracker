package my.training.runtracker.di

import com.example.core_provider.CoreProviderFactory
import dagger.Component
import my.training.core.core_api.di.ProvidersFacade
import my.training.core.core_api.di.UserRepositoryProvider
import my.training.core.core_api.di.scopes.PerApplication

@Component(
    modules = [HomeMediatorModule::class],
    dependencies = [UserRepositoryProvider::class]
)
@PerApplication
interface FacadeComponent : ProvidersFacade {

    companion object {

        private var facadeComponent: FacadeComponent? = null

        fun create(): FacadeComponent {
            return facadeComponent ?: DaggerFacadeComponent.builder()
                .userRepositoryProvider(CoreProviderFactory.createUserRepositoryProvider())
                .build()
                .also {
                    facadeComponent = it
                }
        }
    }

}