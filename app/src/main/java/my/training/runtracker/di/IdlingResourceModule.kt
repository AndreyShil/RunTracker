package my.training.runtracker.di

import androidx.test.espresso.idling.CountingIdlingResource
import dagger.BindsOptionalOf
import dagger.Module

@Module
interface IdlingResourceModule {

    @BindsOptionalOf
    fun provideIdlingResource(): CountingIdlingResource
}