package my.training.feature.stats.presentation.custom_view

import android.content.Context
import android.util.AttributeSet
import android.util.Range
import androidx.constraintlayout.widget.ConstraintLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import my.training.feature.stats.R
import my.training.feature.stats.databinding.ViewStatsBinding
import my.training.feature.stats.domain.model.Stats

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

    private var toggleListener: ((days: Int) -> Unit)? = null

    init {
        inflate(context, R.layout.view_stats, this)
        binding.toggleGroup.check(R.id.btn_one_week)
        initToggleListener()
    }

    fun setData(
        data: List<Stats>
    ) {
        binding.toggleGroup.isClickable = true
        updateStatsData(
            statsData = data
        )
    }

    fun setUpdateListener(callback: (Int) -> Unit) {
        toggleListener = callback
    }

    private fun initToggleListener() {
        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                binding.toggleGroup.isClickable = false
                val count = daysMap[checkedId] ?: 0
                toggleListener?.invoke(count)
            }
        }
    }

    private fun updateStatsData(
        statsData: List<Stats>
    ) {
        val targetId = binding.toggleGroup.checkedButtonId
        val daysCount = daysMap[targetId] ?: 0
        updateData(
            dayCount = daysCount,
            statsData = statsData
        )
    }

    private fun updateData(
        dayCount: Int,
        statsData: List<Stats>
    ) {
        binding.viewChart.setStatsData(statsData, dayCount)

        initCaloriesStats(
            races = statsData,
            dayCount = dayCount
        )

        initAverageSpeedStats(
            races = statsData,
            dayCount = dayCount
        )
    }

    private fun initCaloriesStats(
        races: List<Stats>,
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

        val mediumValue = if (races.isNotEmpty())
            races.sumOf { it.burnedCalories } / races.count()
        else
            0.0

        val fullMediumValue = races.sumOf { it.burnedCalories } / dayCount
        binding.viewCaloriesChart.setDataChart(
            data = resultList.toList().ifEmpty {
                listOf(
                    (0 to context.getString(my.training.core.strings.R.string.no_workouts))
                )
            },
            totalSuffix = context.getString(my.training.core.strings.R.string.cal),
            medianValues = mediumValue to fullMediumValue
        )
    }

    private fun initAverageSpeedStats(
        races: List<Stats>,
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

        val mediumValue = if (races.isNotEmpty())
            races.sumOf { it.averageSpeed } / races.count()
        else
            0.0
        val fullMediumValue = races.sumOf { it.averageSpeed } / dayCount
        binding.viewSpeedChart.setDataChart(
            data = resultList.toList().ifEmpty {
                listOf(
                    (0 to context.getString(my.training.core.strings.R.string.no_workouts))
                )
            },
            totalSuffix = context.getString(my.training.core.strings.R.string.m_per_s),
            medianValues = mediumValue to fullMediumValue
        )
    }

    private fun MutableList<Pair<Int, String>>.addCaloriesItem(
        races: List<Stats>,
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
        races: List<Stats>,
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
}