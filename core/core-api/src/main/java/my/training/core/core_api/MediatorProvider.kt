package my.training.core.core_api

import javax.inject.Provider

interface MediatorProvider {

    fun provideMediatorsMap(): Map<Class<*>, @JvmSuppressWildcards Provider<Any>>

}