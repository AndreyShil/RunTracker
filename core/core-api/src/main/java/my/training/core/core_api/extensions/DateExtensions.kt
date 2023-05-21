package my.training.core.core_api.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val ISO_DATE_PATTERN = "yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'"

fun getCurrentDate(): String {
    val formatter = SimpleDateFormat(ISO_DATE_PATTERN, Locale.getDefault())
    return formatter.format(Date())
}
