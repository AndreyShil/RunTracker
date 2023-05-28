package my.training.core.core_api.extensions

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val ISO_DATE_PATTERN = "yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'"
const val DATE_PATTERN = "dd.MM.yyyy"

fun getCurrentDate(pattern: String = ISO_DATE_PATTERN): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date())
}

fun String.getFormattedDate(
    inPattern: String = ISO_DATE_PATTERN,
    outPattern: String = DATE_PATTERN
): String {
    val date = Date(getMillis(inPattern) ?: return "")
    val formatter = SimpleDateFormat(outPattern, Locale.getDefault())
    return formatter.format(date)
}

fun String.getMillis(pattern: String = ISO_DATE_PATTERN): Long? {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.parse(this)?.time
}

fun String.getDisplayedDate(pattern: String = DATE_PATTERN): String {
    return when {
        isToday(pattern) -> "Сегодня"
        isYesterday(pattern) -> "Вчера"
        else -> this
    }
}

fun String.isToday(pattern: String = DATE_PATTERN): Boolean {
    return DateUtils.isToday(getMillis(pattern) ?: return false)
}

fun String.isYesterday(pattern: String = DATE_PATTERN): Boolean {
    val millis = getMillis(pattern) ?: return false
    return DateUtils.isToday(millis + DateUtils.DAY_IN_MILLIS)
}