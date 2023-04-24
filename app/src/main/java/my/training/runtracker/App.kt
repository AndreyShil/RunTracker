package my.training.runtracker

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import my.training.core.core_api.AppWithFacade
import my.training.core.core_api.ProvidersFacade
import my.training.runtracker.di.FacadeComponent

class App : Application(), AppWithFacade {

//    private var facadeComponent: FacadeComponent? = null

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(getString(R.string.map_key))
    }

    override fun getFacade(): ProvidersFacade {
        return FacadeComponent.create()
    }
}