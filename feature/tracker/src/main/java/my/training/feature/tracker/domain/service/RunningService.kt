package my.training.feature.tracker.domain.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

internal class RunningService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }



}