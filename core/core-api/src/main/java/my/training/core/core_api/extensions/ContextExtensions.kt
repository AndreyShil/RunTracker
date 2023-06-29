package my.training.core.core_api.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat

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

fun Context.hasLocationPermission(): Boolean {
    return isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) &&
            isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
}

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}