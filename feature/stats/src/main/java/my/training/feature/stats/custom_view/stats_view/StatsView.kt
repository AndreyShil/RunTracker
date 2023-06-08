package my.training.feature.stats.custom_view.stats_view

import android.content.Context
import android.util.AttributeSet
import android.util.Range
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import my.training.feature.stats.MockData
import my.training.feature.stats.R
import my.training.feature.stats.databinding.ViewStatsBinding
import my.training.feature.stats.model.RaceData
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

private const val THREE_MONTH_DAYS = 90
private const val ONE_MONTH_DAYS = 30
private const val ONE_WEEK_DAYS = 7

private const val VALUE_0 = 0.0
private const val VALUE_100 = 100.0
private const val VALUE_200 = 200.0
private const val VALUE_300 = 300.0

private const val VALUE_5 = 5.0
private const val VALUE_7_5 = 7.5
private const val VALUE_10 = 10.0

internal class StatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val binding by viewBinding(ViewStatsBinding::bind)

    private val daysMap = mapOf(
        R.id.btn_three_month to THREE_MONTH_DAYS,
        R.id.btn_one_month to ONE_MONTH_DAYS,
        R.id.btn_one_week to ONE_WEEK_DAYS
    )

    private val currentCalendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        .apply {
            clearTime()
        }

    init {
        inflate(context, R.layout.view_stats, this)
        binding.toggleGroup.check(R.id.btn_one_week)
        updateStatsData(R.id.btn_one_week)
        initToggleListener()
    }

    private fun initToggleListener() {
        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                updateStatsData(checkedId)
            }
        }
    }

    private fun updateStatsData(@IdRes checkedId: Int) {
        val daysCount = daysMap[checkedId] ?: 0
        updateData(daysCount)
    }

    private fun updateData(dayCount: Int) {
        val startDateRangeCalendar = getCalendarInstanceWithDecreaseDays(dayCount)

        val filteredData = MockData.statsData
            .filter {
                it.getCalendarInstance()
                    ?.apply {
                        clearTime()
                    }
                    ?.after(startDateRangeCalendar) == true
            }

        binding.viewChart.setStatsData(filteredData, dayCount)

        initCaloriesStats(
            races = filteredData,
            dayCount = dayCount
        )

        initAverageSpeedStats(
            races = filteredData,
            dayCount = dayCount
        )
    }

    private fun initCaloriesStats(
        races: List<RaceData>,
        dayCount: Int
    ) {
        val resultList = buildList {
            addCaloriesItem(
                races = races,
                range = Range(VALUE_0, VALUE_100),
                description = context.getString(my.training.core.strings.R.string.interval_below_100_cal)
            )
            addCaloriesItem(
                races = races,
                range = Range(VALUE_100, VALUE_200),
                description = context.getString(my.training.core.strings.R.string.interval_100_200_cal)
            )
            addCaloriesItem(
                races = races,
                range = Range(VALUE_200, VALUE_300),
                description = context.getString(my.training.core.strings.R.string.interval_200_300_cal)
            )
            addCaloriesItem(
                races = races,
                range = Range(VALUE_300, Double.MAX_VALUE),
                description = context.getString(my.training.core.strings.R.string.interval_above_300_cal)
            )
        }

        val mediumValue = races.sumOf { it.burnedCalories } / dayCount
        val fullMediumValue = races.sumOf { it.burnedCalories } / races.count()
        binding.viewCaloriesChart.setDataChart(
            data = resultList.toList().ifEmpty {
                listOf(
                    (0 to context.getString(my.training.core.strings.R.string.interval_below_100_cal))
                )
            },
            totalSuffix = context.getString(my.training.core.strings.R.string.cal),
            medianValues = mediumValue to fullMediumValue
        )
    }

    private fun initAverageSpeedStats(
        races: List<RaceData>,
        dayCount: Int
    ) {
        val resultList = buildList {
            addSpeedItem(
                races = races,
                range = Range(VALUE_0, VALUE_5),
                description = context.getString(my.training.core.strings.R.string.interval_below_5_m_per_s)
            )
            addSpeedItem(
                races = races,
                range = Range(VALUE_5, VALUE_7_5),
                description = context.getString(my.training.core.strings.R.string.interval_5_7_5_m_per_s)
            )
            addSpeedItem(
                races = races,
                range = Range(VALUE_7_5, VALUE_10),
                description = context.getString(my.training.core.strings.R.string.interval_7_5_10_m_per_s)
            )
            addSpeedItem(
                races = races,
                range = Range(VALUE_10, Double.MAX_VALUE),
                description = context.getString(my.training.core.strings.R.string.interval_above_10_m_per_s)
            )
        }

        val mediumValue = races.sumOf { it.averageSpeed } / races.count()
        val fullMediumValue = races.sumOf { it.averageSpeed } / dayCount
        binding.viewSpeedChart.setDataChart(
            data = resultList.toList().ifEmpty {
                listOf(
                    (0 to context.getString(my.training.core.strings.R.string.interval_below_5_m_per_s))
                )
            },
            totalSuffix = context.getString(my.training.core.strings.R.string.m_per_s),
            medianValues = mediumValue to fullMediumValue
        )
    }

    private fun MutableList<Pair<Int, String>>.addCaloriesItem(
        races: List<RaceData>,
        range: Range<Double>,
        description: String
    ) {
        val count =
            races.count { it.burnedCalories.inRangeExcludeUpperValue(range) }
        if (count > 0) {
            add(
                (count to description)
            )
        }
    }

    private fun MutableList<Pair<Int, String>>.addSpeedItem(
        races: List<RaceData>,
        range: Range<Double>,
        description: String
    ) {
        val count =
            races.count { it.averageSpeed.inRangeExcludeUpperValue(range) }
        if (count > 0) {
            add(
                (count to description)
            )
        }
    }

    private fun Double.inRangeExcludeUpperValue(range: Range<Double>): Boolean {
        return this >= range.lower && this < range.upper
    }

    private fun getCalendarInstanceWithDecreaseDays(dayCount: Int): Calendar {
        return Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
            .apply {
                set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR))
                set(Calendar.DAY_OF_YEAR, currentCalendar.get(Calendar.DAY_OF_YEAR) - dayCount)
                clearTime()
            }
    }

    private fun Calendar.clearTime() {
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}
