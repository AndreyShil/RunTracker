package my.training.core.core_api.domain.manager

import androidx.annotation.StringRes

interface ResourcesManager {
    fun getString(@StringRes resId: Int): String
    fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String
}