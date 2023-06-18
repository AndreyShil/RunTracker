package my.training.core.core_api.domain.preferences

import my.training.core.core_api.domain.model.enums.AppTheme

interface Preferences {

    fun saveAccessToken(token: String)
    fun getAccessToken(): String?

    fun updateAppTheme(appTheme: AppTheme)
    fun getAppTheme(): AppTheme

    fun clear()
}