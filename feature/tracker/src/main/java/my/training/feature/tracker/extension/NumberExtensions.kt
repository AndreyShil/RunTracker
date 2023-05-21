package my.training.feature.tracker.extension

import java.util.concurrent.TimeUnit

internal fun Long.getFormattedWatchTime(includeMillis: Boolean = false): String {
    var milliseconds = this
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    milliseconds -= TimeUnit.HOURS.toMillis(hours)

    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
    milliseconds -= TimeUnit.MINUTES.toMillis(minutes)

    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

    if (!includeMillis) {
        return "${String.format("%02d", hours)}:" +
                "${String.format("%02d", minutes)}:" +
                String.format("%02d", seconds)
    }
    milliseconds -= TimeUnit.SECONDS.toMillis(seconds)

    return "${String.format("%02d", hours)}:" +
            "${String.format("%02d", minutes)}:" +
            "${String.format("%02d", seconds)}:" +
            String.format("%03d", milliseconds)
}

internal fun Int.displayDistance(): String {
    return if (this >= 1000)
        String.format("%.2f", this / 1000.0) + " км"
    else
        "$this м"
}

internal fun Double.displaySpeed(): String {
    return String.format("%.1f", this) + " м/с"
}

internal fun Double.displayCalories(): String {
    return String.format("%.1f", this) + " Ккал"
}