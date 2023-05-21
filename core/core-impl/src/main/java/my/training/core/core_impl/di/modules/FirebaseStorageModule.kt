package my.training.core.core_impl.di.modules

import dagger.Binds
import dagger.Module
import my.training.core.core_api.domain.manager.FirebaseStorageManager
import my.training.core.core_impl.data.manager.FirebaseStorageManagerImpl

@Module
internal interface FirebaseStorageModule {

    @Binds
    fun bindFirebaseStorageManager(impl: FirebaseStorageManagerImpl): FirebaseStorageManager
}