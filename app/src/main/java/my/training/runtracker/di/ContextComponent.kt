package my.training.runtracker.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import my.training.core.core_api.di.ContextProvider
import my.training.core.core_api.di.qualifiers.AppContext
import my.training.core.core_api.di.scopes.PerApplication

@Component
@PerApplication
interface ContextComponent : ContextProvider {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance @AppContext context: Context): ContextComponent
    }

    companion object {
        private var contextComponent: ContextComponent? = null

        fun create(context: Context): ContextComponent {
            return contextComponent ?: DaggerContextComponent
                .factory()
                .create(context)
                .also { contextComponent = it }
        }
    }
}
