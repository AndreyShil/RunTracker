package my.training.runtracker.di

import dagger.Component
import my.training.core.core_api.ProvidersFacade
import my.training.core.core_api.scopes.PerApplication

@Component(
    modules = [HomeMediatorModule::class]
)
@PerApplication
interface FacadeComponent : ProvidersFacade {

    companion object {

        private var facadeComponent: FacadeComponent? = null

        fun create(): FacadeComponent {
            return facadeComponent ?: DaggerFacadeComponent.builder().build().also {
                facadeComponent = it
            }
        }
    }

}