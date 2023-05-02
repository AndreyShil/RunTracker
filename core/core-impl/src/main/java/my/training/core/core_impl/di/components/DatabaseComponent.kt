package my.training.core.core_impl.di.components

import dagger.Component
import my.training.core.core_api.di.ContextProvider
import my.training.core.core_api.di.DatabaseProvider
import my.training.core.core_api.di.scopes.PerApplication
import my.training.core.core_impl.di.modules.DatabaseModule

@Component(
    modules = [DatabaseModule::class],
    dependencies = [ContextProvider::class]
)
@PerApplication
interface DatabaseComponent : DatabaseProvider {

    companion object {
        private var component: DatabaseComponent? = null

        fun create(contextProvider: ContextProvider): DatabaseComponent {
            return component ?: DaggerDatabaseComponent.builder()
                .contextProvider(contextProvider)
                .build()
                .also { component = it }
        }
    }
}