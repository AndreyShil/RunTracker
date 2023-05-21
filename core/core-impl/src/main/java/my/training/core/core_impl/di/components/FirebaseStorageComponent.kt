package my.training.core.core_impl.di.components

import dagger.Component
import my.training.core.core_api.di.FirebaseStorageProvider
import my.training.core.core_impl.di.modules.FirebaseStorageModule

@Component(
    modules = [FirebaseStorageModule::class]
)
interface FirebaseStorageComponent: FirebaseStorageProvider {

    companion object {
        fun create(): FirebaseStorageComponent {
            return DaggerFirebaseStorageComponent.builder().build()
        }
    }
}