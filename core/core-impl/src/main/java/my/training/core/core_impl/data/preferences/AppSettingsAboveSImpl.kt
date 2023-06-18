package my.training.core.core_impl.data.preferences

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import my.training.core.core_api.di.qualifiers.AppContext
import my.training.core.core_api.domain.model.enums.AppTheme
import my.training.core.core_api.domain.preferences.AppSettings
import my.training.core.core_api.domain.preferences.Preferences
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.S)
internal class AppSettingsAboveSImpl @Inject constructor(
    @AppContext private val context: Context,
    private val preferences: Preferences
) : AppSettings {

    override fun installTheme() {
        setAppTheme(
            getAppTheme()
        )
    }

    override fun setAppTheme(appTheme: AppTheme) {
        val manager = context.getSystemService(UiModeManager::class.java)
        when (appTheme) {
            AppTheme.SYSTEM -> {
                manager?.setApplicationNightMode(UiModeManager.MODE_NIGHT_AUTO)
            }

            AppTheme.LIGHT_MODE -> {
                manager?.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
            }

            AppTheme.DARK_MODE -> {
                manager?.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
            }
        }
        preferences.updateAppTheme(appTheme)
    }

    override fun getAppTheme(): AppTheme {
        return preferences.getAppTheme()
    }
}