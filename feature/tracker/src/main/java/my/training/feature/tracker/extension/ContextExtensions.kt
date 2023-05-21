package my.training.feature.tracker.extension

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.hasLocationPermission(): Boolean {
    return isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) &&
            isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
}

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}