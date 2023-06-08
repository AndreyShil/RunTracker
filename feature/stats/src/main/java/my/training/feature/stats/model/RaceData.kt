package my.training.feature.stats.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import my.training.core.core_api.extensions.getMillis
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Parcelize
internal data class RaceData(
    val date: String,
    val distance: Int,
    val burnedCalories: Double,
    val averageSpeed: Double
) : Parcelable {

    fun getCalendarInstance(): Calendar? {
        val date = getDateInstance() ?: return null
        return Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
            .apply {
                time = date
            }
    }

    fun getStartDayDate(): Date? {
        val date = getDateInstance() ?: return null
        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.time
    }

    private fun getDateInstance(): Date? {
        return date.getMillis()?.let { Date(it) }
    }
}
