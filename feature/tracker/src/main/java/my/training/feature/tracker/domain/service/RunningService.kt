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
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import my.training.core.core_api.di.ProvidersHolder
import my.training.core.iconpack.R
import my.training.feature.tracker.di.RunningServiceComponent
import my.training.feature.tracker.domain.model.RunningState
import my.training.feature.tracker.extension.getFormattedWatchTime
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

    private val _state = MutableLiveData(RunningState.INITIAL)
    val state: LiveData<RunningState> = _state

    private val _timeRunInMillis = MutableLiveData(0L)
    val timeRunInMillis: LiveData<Long> = _timeRunInMillis

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L

    private var lastLocationString = ""

    private fun startTimer() {
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        serviceScope.launch {
            while (isTimerEnabled) {
                lapTime = System.currentTimeMillis() - timeStarted

                withContext(Dispatchers.Main) {
                    _timeRunInMillis.value = timeRun + lapTime
                    updateNotificationTime(timeRunInMillis.value)
                }
                delay(50)
            }
            timeRun += lapTime

        }
    }

    private fun resetTimeParams() {
        lapTime = 0L
        timeRun = 0L
        _timeRunInMillis.value = 0L
    }

    override fun onBind(intent: Intent?): IBinder {
        return LocationBinder()
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
        _state.value = RunningState.IN_PROGRESS
        startTimer()

        val notification = notificationHelper.getNotificationBuilder()
        notification.createNotification()

        locationClient
            .getLocationUpdates()
            .catch { e ->
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                }
                e.printStackTrace()
            }
            .onEach { location ->
                locations.add(location)
            }
            .launchIn(serviceScope)

        startForeground(NOTIFICATION_ID, notification.build())
    }

    private fun NotificationCompat.Builder.createNotification() {
        val notificationManager = getSystemService<NotificationManager>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationHelper.createNotificationChannel(notificationManager)
        }

        addPendingIntents()
    }

    private fun NotificationCompat.Builder.addPendingIntents() {
        val resumePendingIntent = createPendingIntent(
            action = ACTION_START_OR_RESUME_SERVICE,
            requestCode = 1
        )

        val pausePendingIntent = createPendingIntent(
            action = ACTION_PAUSE_SERVICE,
            requestCode = 2
        )

        val stopPendingIntent = createPendingIntent(
            action = ACTION_STOP_SERVICE,
            requestCode = 3
        )

        addAction(R.drawable.ic_play_40, "Начать", resumePendingIntent)
        addAction(R.drawable.ic_pause_40, "Пауза", pausePendingIntent)
        addAction(R.drawable.ic_stop_40, "Стоп", stopPendingIntent)
    }

    private fun updateNotificationTime(currentTime: Long?) {
        val notification = notificationHelper.getNotificationBuilder()
        val notificationManager = getSystemService<NotificationManager>()
        notification.addPendingIntents()

        val updatedNotification = notification
            .setContentTitle(
                "Running - " + currentTime?.getFormattedWatchTime()
            )
            .setContentText(lastLocationString)
        notificationManager?.notify(NOTIFICATION_ID, updatedNotification.build())
    }

    private fun createPendingIntent(
        action: String,
        requestCode: Int
    ): PendingIntent {
        val intent = Intent(this, RunningService::class.java).apply {
            this.action = action
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getService(
            this,
            requestCode,
            intent,
            FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
        )
    }

    private fun pauseRunning() {
        isTimerEnabled = false
        locationsCallback?.invoke(locations.toList())
        _state.value = RunningState.ON_PAUSE
    }

    private fun stopRunning() {
        isTimerEnabled = false

        locationsCallback?.invoke(locations.toList())
        unbindServiceCallback?.invoke()
        _state.value = RunningState.FINISH

        resetTimeParams()
        locations.clear()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    inner class LocationBinder : Binder() {
        fun getService(): RunningService = this@RunningService

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
    }

}