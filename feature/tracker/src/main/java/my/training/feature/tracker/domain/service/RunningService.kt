package my.training.feature.tracker.domain.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import my.training.core.core_api.di.ProvidersHolder
import my.training.feature.tracker.di.RunningServiceComponent
import javax.inject.Inject

typealias locationsCallback = (List<Location>) -> Unit

internal class RunningService : Service() {

    @Inject
    lateinit var locationClient: LocationClient

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var locationsCallback: (locationsCallback)? = null
    private var unbindServiceCallback: (() -> Unit)? = null

    private val locations = mutableListOf<Location>()

//    private val _locationsData = MutableLiveData(emptyList<Location>())
//    val locationsData: LiveData<List<Location>> = _locationsData

    // Binder given to clients (notice class declaration below)
    private val mBinder: IBinder = LocationBinder()

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()

        RunningServiceComponent
            .create((application as ProvidersHolder).getAggregatingProvider())
            .inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> startRunning()
                ACTION_PAUSE_SERVICE -> pauseRunning()
                ACTION_STOP_SERVICE -> stopRunning()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startRunning() {
        val notification = notificationHelper.getNotificationBuilder()
        val notificationManager = getSystemService<NotificationManager>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationHelper.createNotificationChannel(notificationManager)
        }

        val resumeIntent = Intent(this, RunningService::class.java).apply {
            action = ACTION_START_OR_RESUME_SERVICE
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val resumePendingIntent =
            PendingIntent.getService(this, 1, resumeIntent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val stopIntent = Intent(this, RunningService::class.java).apply {
            action = ACTION_STOP_SERVICE
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val stopPendingIntent =
            PendingIntent.getService(this, 2, stopIntent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        notification
            .addAction(android.R.drawable.ic_media_play, "Start", resumePendingIntent)
            .addAction(android.R.drawable.ic_media_pause, "Stop", stopPendingIntent)

        locationClient
            .getLocationUpdates(LOCATION_UPDATE_INTERVAL)
            .catch { e ->
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                }
                e.printStackTrace()
            }
            .onEach { location ->
                locations.add(location)

                val lat = location.latitude
                val long = location.longitude
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                notificationManager?.notify(NOTIFICATION_ID, updatedNotification.build())
            }
            .launchIn(serviceScope)

        startForeground(NOTIFICATION_ID, notification.build())
    }

    private fun pauseRunning() {

    }

    private fun stopRunning() {
        locationsCallback?.invoke(locations.toList())
//
        unbindServiceCallback?.invoke()
//        _locationsData.value = locations.toList()

        locations.clear()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }


    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionText = if (isTracking) "Pause" else "Resume"
        val pendingIntent = if (isTracking) {
            val pauseIntent = Intent(this, RunningService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, FLAG_UPDATE_CURRENT)
        } else {
            val resumeIntent = Intent(this, RunningService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(this, 2, resumeIntent, FLAG_UPDATE_CURRENT)
        }

//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
//            isAccessible = true
//            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
//        }
//        if (!serviceKilled) {
//            curNotificationBuilder = baseNotificationBuilder
//                .addAction(R.drawable.ic_pause_black_24dp, notificationActionText, pendingIntent)
//            notificationManager.notify(NOTIFICATION_ID, curNotificationBuilder.build())
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    inner class LocationBinder : Binder() {
//        fun getService(): RunningService = this@RunningService

        fun unbindService(callback: () -> Unit) {
            this@RunningService.unbindServiceCallback = callback
        }
        fun subscribeToLocations(locationsCallback: locationsCallback) {
            this@RunningService.locationsCallback = locationsCallback
        }
    }

    companion object {
        const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_SERVICE"
        const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"

        private const val NOTIFICATION_ID = 1
        private const val LOCATION_UPDATE_INTERVAL = 5_000L
    }

}