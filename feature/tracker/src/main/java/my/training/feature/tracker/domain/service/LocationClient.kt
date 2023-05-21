package my.training.feature.tracker.domain.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import my.training.core.core_api.di.qualifiers.AppContext
import my.training.feature.tracker.extension.hasLocationPermission
import javax.inject.Inject

internal class LocationClient @Inject constructor(
    @AppContext private val appContext: Context,
    private val client: FusedLocationProviderClient
) {

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(): Flow<Location> {
        return callbackFlow {
            checkLocationDetermineEnabled()
            val request = LocationRequest.create()
                .setInterval(LOCATION_UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_LOCATION_INTERVAL)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }

    private fun checkLocationDetermineEnabled() {
        if (!appContext.hasLocationPermission()) {
            throw RuntimeException("Отсутствуют разрешения на доступ к местоположению")
        }

        val locationManager = appContext.getSystemService(LocationManager::class.java)
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGpsEnabled && !isNetworkEnabled) {
            throw RuntimeException("Местоположение отключено")
        }
    }

    companion object {
        private const val LOCATION_UPDATE_INTERVAL = 5_000L
        private const val FASTEST_LOCATION_INTERVAL = 4_000L
    }

}