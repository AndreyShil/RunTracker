package my.training.core.core_api.di

import androidx.test.espresso.idling.CountingIdlingResource
import java.util.Optional

interface IdlingResourceProvider {
    fun provideIdlingResource(): Optional<CountingIdlingResource>
}