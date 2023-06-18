package my.training.core.core_impl.data.preferences

import androidx.appcompat.app.AppCompatDelegate
import my.training.core.core_api.domain.model.enums.AppTheme
import my.training.core.core_api.domain.preferences.AppSettings
import my.training.core.core_api.domain.preferences.Preferences
import javax.inject.Inject

internal class AppSettingsBelowSImpl @Inject constructor(
    private val preferences: Preferences
) : AppSettings {

    override fun installTheme() {
        setAppTheme(
            getAppTheme()
        )
    }

    override fun setAppTheme(appTheme: AppTheme) {
        when (appTheme) {
            AppTheme.SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }

            AppTheme.LIGHT_MODE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            AppTheme.DARK_MODE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        preferences.updateAppTheme(appTheme)
    }

    override fun getAppTheme(): AppTheme {
        return preferences.getAppTheme()
    }
}