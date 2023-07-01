package my.training.runtracker

import my.training.core.core_api.di.AggregatingProvider
import my.training.core.core_api.di.ProvidersHolder

class TestApp : App(), ProvidersHolder {

    override fun getAggregatingProvider(): AggregatingProvider {
        return TestAggregatingComponent.create(this)
    }
}