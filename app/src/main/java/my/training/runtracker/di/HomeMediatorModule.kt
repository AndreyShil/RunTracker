package my.training.runtracker.di

import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import my.training.core.core_api.HomeMediator

@Module
interface HomeMediatorModule {

    @Binds
    @IntoMap
    @ClassKey(HomeMediator::class)
    fun bindMediator(mediator: HomeMediatorImpl): Any
}