package my.training.feature.stats.custom_view.stats_view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import my.training.feature.stats.R
import my.training.feature.stats.MockData
import my.training.feature.stats.databinding.ViewStatsBinding

internal class StatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val binding by viewBinding(ViewStatsBinding::bind)

    private val daysMap = mapOf(
        R.id.btn_three_month to 90,
        R.id.btn_one_month to 30,
        R.id.btn_one_week to 7
    )

    init {
        inflate(context, R.layout.view_stats, this)
        binding.toggleGroup.check(R.id.btn_one_week)
        updateChartData(R.id.btn_one_week)
        initToggleListener()
        binding.viewChart.setStatsData(MockData.statsData)
    }

    private fun initToggleListener() {
        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                updateChartData(checkedId)
            }
        }
    }

    private fun updateChartData(@IdRes checkedId: Int) {
        val daysCount = daysMap[checkedId] ?: 0
        binding.viewChart.updateDaysCount(daysCount)
    }

}