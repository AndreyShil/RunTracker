package my.training.runtracker

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import my.training.core.core_api.di.AggregatingProvider
import my.training.core.core_api.di.ProvidersHolder
import my.training.runtracker.di.AggregatingComponent

open class App : Application(), ProvidersHolder {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(getString(R.string.map_key))
    }

    override fun getAggregatingProvider(): AggregatingProvider {
        return AggregatingComponent.create(this)
    }
}