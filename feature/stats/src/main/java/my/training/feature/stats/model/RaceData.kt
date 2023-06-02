package my.training.feature.stats.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import my.training.core.core_api.extensions.getMillis
import java.util.Calendar
import java.util.Date

@Parcelize
internal data class RaceData(
    val date: String,
    val distance: Int,
    val burnedCalories: Double
) : Parcelable {

    fun getDateInstance(): Date? {
        return date.getMillis()?.let { Date(it) }
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
}