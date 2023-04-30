package my.training.core.core_impl.di.modules

import dagger.Binds
import dagger.Module
import my.training.core.core_api.domain.repository.UserRepository
import my.training.core.core_impl.repository.UserRepositoryImpl

@Module
internal interface UserRepositoryModule {

    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

}