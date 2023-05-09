package my.training.core.core_api.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.provider.Settings

@SuppressLint("HardwareIds")
fun Context.getDeviceId(): String {
    return Settings.Secure.getString(
        contentResolver,
        Settings.Secure.ANDROID_ID
    )
}

fun Context.isNightModeActive(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        resources.configuration.isNightModeActive
    } else {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
}