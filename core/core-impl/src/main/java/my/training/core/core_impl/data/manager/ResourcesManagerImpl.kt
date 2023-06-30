package my.training.core.core_impl.data.manager

import android.content.Context
import my.training.core.core_api.di.qualifiers.AppContext
import my.training.core.core_api.domain.manager.ResourcesManager
import javax.inject.Inject

internal class ResourcesManagerImpl @Inject constructor(
    @AppContext private val context: Context
) : ResourcesManager {

    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg formatArgs: Any?): String {
        return context.getString(resId, *formatArgs)
    }
}