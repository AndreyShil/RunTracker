package my.training.core.core_impl.di.components

import dagger.Component
import my.training.core.core_api.di.ContextProvider
import my.training.core.core_api.di.UserRepositoryProvider
import my.training.core.core_api.di.scopes.PerApplication
import my.training.core.core_impl.di.modules.DatabaseModule
import my.training.core.core_impl.di.modules.NetworkModule
import my.training.core.core_impl.di.modules.UserRepositoryModule

@Component(
    modules = [
        NetworkModule::class,
        DatabaseModule::class,
        UserRepositoryModule::class
    ],
    dependencies = [ContextProvider::class]
)
@PerApplication
interface UserRepositoryComponent : UserRepositoryProvider {

    companion object {
        fun create(contextProvider: ContextProvider): UserRepositoryComponent {
            return DaggerUserRepositoryComponent.builder()
                .contextProvider(contextProvider)
                .build()
        }
    }
}