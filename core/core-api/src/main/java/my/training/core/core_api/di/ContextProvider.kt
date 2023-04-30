package my.training.core.core_api.di

import android.content.Context
import my.training.core.core_api.di.qualifiers.AppContext

interface ContextProvider {

    @AppContext
    fun provideContext(): Context
}
