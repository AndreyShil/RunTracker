package my.training.core.core_api.domain.preferences

import my.training.core.core_api.domain.model.enums.AppTheme

interface AppSettings {
    fun installTheme()
    fun setAppTheme(appTheme: AppTheme)
    fun getAppTheme(): AppTheme
}