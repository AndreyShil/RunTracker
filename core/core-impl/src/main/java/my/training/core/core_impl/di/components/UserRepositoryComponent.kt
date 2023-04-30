package my.training.core.core_impl.di.components

import dagger.Component
import my.training.core.core_api.di.UserRepositoryProvider
import my.training.core.core_impl.di.modules.NetworkModule
import my.training.core.core_impl.di.modules.UserRepositoryModule

@Component(
    modules = [
        NetworkModule::class,
        UserRepositoryModule::class
    ]
)
interface UserRepositoryComponent : UserRepositoryProvider {

    companion object {
        fun create(): UserRepositoryComponent {
            return DaggerUserRepositoryComponent.builder().build()
        }
    }
}