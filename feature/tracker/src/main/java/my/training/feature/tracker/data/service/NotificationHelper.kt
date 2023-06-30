package my.training.feature.tracker.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import my.training.core.core_api.di.qualifiers.AppContext
import my.training.core.core_api.domain.manager.ResourcesManager
import javax.inject.Inject
import my.training.core.iconpack.R as iconsR
import my.training.core.strings.R as stringsR

internal class NotificationHelper @Inject constructor(
    @AppContext private val context: Context,
    private val resourcesManager: ResourcesManager
) {

    fun getNotificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(resourcesManager.getString(stringsR.string.workout_was_started))
            .setSmallIcon(iconsR.drawable.ic_run_40)
            .setOngoing(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(notificationManager: NotificationManager?) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager?.createNotificationChannel(channel)
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
        private const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    }
}