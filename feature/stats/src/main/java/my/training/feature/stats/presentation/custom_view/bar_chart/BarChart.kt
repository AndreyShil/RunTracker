package my.training.feature.stats.presentation.custom_view.bar_chart

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import my.training.core.core_api.extensions.readParcelList
import my.training.feature.stats.R
import my.training.feature.stats.data.model.Stats
import java.util.Calendar
import java.util.Locale
import kotlin.math.min
import kotlin.math.roundToInt

private const val CHART_SIZE_RATIO = 0.6f
private const val DASH_PHASE = 0f
private const val EMPTY_BAR_COLOR_ALPHA = 60
private const val BAR_HEIGHT_RATIO = 1.1

internal class BarChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val defaultPadding by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(my.training.core.ui.R.dimen.padding_md)
    }

    private val barRadius by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(my.training.core.ui.R.dimen.card_radius_xs)
    }

    private val lineWidth by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(R.dimen.chart_stroke_width)
    }

    private val dashSize by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(R.dimen.chart_stroke_dash_size)
    }

    private val textBorderInset by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(R.dimen.chart_text_border_inset)
    }

    private val textBorderWidth by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(R.dimen.chart_text_stroke_width)
    }

    private val textBorderCornerRadius by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(R.dimen.chart_text_border_corner_radius)
    }

    private val medianTextInset by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(my.training.core.ui.R.dimen.padding_xxs)
    }

    private val medianTextSize by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(my.training.core.ui.R.dimen.text_size_md_plus)
    }

    private val mainTextColor by lazy(LazyThreadSafetyMode.NONE) {
        ContextCompat.getColor(context, R.color.main_text_color)
    }

    private val additionalTextColor by lazy(LazyThreadSafetyMode.NONE) {
        ContextCompat.getColor(context, R.color.secondary_text_color)
    }

    private val lightColor by lazy(LazyThreadSafetyMode.NONE) {
        ContextCompat.getColor(context, R.color.bar_chart_text_bg_color)
    }

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.bar_chart_bar_color)
    }

    private val emptyBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.bar_chart_empty_bar_color)
        alpha = EMPTY_BAR_COLOR_ALPHA
    }

    private val mainLinePaint = getDefaultLinePaint().apply {
        color = mainTextColor
    }

    private val additionalLinePaint = getDefaultLinePaint().apply {
        color = additionalTextColor
    }

    private val mainTextPaint = getDefaultTextPaint().apply {
        color = mainTextColor
    }

    private val additionalTextPaint = getDefaultTextPaint().apply {
        color = additionalTextColor
    }

    private val textBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = textBorderWidth
        color = lightColor
    }

    private fun getDefaultLinePaint(): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = lineWidth
            strokeCap = Paint.Cap.ROUND
            pathEffect = DashPathEffect(floatArrayOf(dashSize, dashSize), DASH_PHASE)
        }
    }

    private fun getDefaultTextPaint(): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            textSize = medianTextSize
            textAlignment = TEXT_ALIGNMENT_CENTER
        }
    }

    private var chartData: List<Stats> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private var dayCount = 0

    private val currentCalendar = Calendar.getInstance(Locale.getDefault())
        .apply {
            clearTime()
        }

    /**
     * Data setting methods
     */
    fun setStatsData(
        stats: List<Stats>,
        dayCount: Int
    ) {
        chartData = stats
        this.dayCount = dayCount
        invalidate()
    }

    /**
     * Measure view methods
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredSize = calculateDesiredSize(widthSpecSize, heightSpecSize)

        val width = getMeasuredDimension(
            specMode = widthSpecMode,
            specSize = widthSpecSize,
            desiredSize = desiredSize.roundToInt()
        )
        val height = getMeasuredDimension(
            specMode = heightSpecMode,
            specSize = heightSpecSize,
            desiredSize = desiredSize.roundToInt()
        )

        setMeasuredDimension(width, height)
    }

    private fun getMeasuredDimension(
        specMode: Int,
        specSize: Int,
        desiredSize: Int
    ): Int {
        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> min(desiredSize, specSize)
            else -> desiredSize
        }
    }

    private fun calculateDesiredSize(
        widthSpecSize: Int,
        heightSpecSize: Int
    ): Float {
        return if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (heightSpecSize >= widthSpecSize) {
                heightSpecSize * CHART_SIZE_RATIO
            } else {
                widthSpecSize * CHART_SIZE_RATIO
            }
        } else {
            if (heightSpecSize >= widthSpecSize) {
                resources.displayMetrics.widthPixels * CHART_SIZE_RATIO
            } else {
                resources.displayMetrics.heightPixels * CHART_SIZE_RATIO
            }
        }
    }

    /**
     * Drawing methods
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawBarChart()
    }

    private fun Canvas.drawBarChart() {
        var barStartX = defaultPadding

        val maxValue = chartData.maxOfOrNull { it.distance } ?: 0
        val barUnitY = (height / (BAR_HEIGHT_RATIO * maxValue))
        val barWidth = (width - 2 * defaultPadding) / (dayCount * 2 - 1)

        val localCalendar = currentCalendar.clone() as? Calendar
        for (index in 0 until dayCount) {
            val targetCalendar = localCalendar?.getCalendarInstanceByTargetIndex(index) ?: continue

            val targetRace = chartData.find { it.getStartDayDate() == targetCalendar.time }
            val calendar = targetRace?.getCalendarInstance()

            drawBars(
                isTargetDay = isSameDay(calendar, targetCalendar),
                raceData = targetRace,
                barStartX = barStartX,
                barWidth = barWidth,
                unitY = barUnitY
            )

            barStartX += (2 * barWidth)
        }

        drawMedianInfoLines(
            raceItems = chartData,
            unitY = barUnitY
        )
    }

    private fun Canvas.drawBars(
        isTargetDay: Boolean,
        raceData: Stats?,
        barStartX: Float,
        barWidth: Float,
        unitY: Double
    ) {
        if (isTargetDay && raceData != null) {
            drawRoundRect(
                barStartX,
                (height - (raceData.distance * unitY)).toFloat(),
                barStartX + barWidth,
                height.toFloat(),
                barRadius,
                barRadius,
                barPaint
            )
        } else {
            drawRoundRect(
                barStartX,
                0f,
                barStartX + barWidth,
                height.toFloat(),
                barRadius,
                barRadius,
                emptyBarPaint
            )
        }
    }

    private fun Canvas.drawMedianInfoLines(
        raceItems: List<Stats>,
        unitY: Double
    ) {
        val activeMedianValue = raceItems.sumOf { it.distance }.toFloat() / raceItems.count()
        val fullMedianValue = raceItems.sumOf { it.distance }.toFloat() / dayCount
        drawLineWithValue(
            value = activeMedianValue,
            unitY = unitY,
            linePaintInstance = mainLinePaint,
            textPaintInstance = mainTextPaint
        )

        drawLineWithValue(
            value = fullMedianValue,
            unitY = unitY,
            linePaintInstance = additionalLinePaint,
            textPaintInstance = additionalTextPaint
        )
    }

    private fun Canvas.drawLineWithValue(
        value: Float,
        unitY: Double,
        linePaintInstance: Paint,
        textPaintInstance: Paint
    ) {
        val medianY = value * unitY
        val lineY = (height - medianY).toFloat()

        drawLine(
            0f,
            lineY,
            width.toFloat(),
            lineY,
            linePaintInstance
        )

        drawLineText(
            value = value,
            textPaintInstance = textPaintInstance,
            lineY = lineY
        )
    }

    private fun Canvas.drawLineText(
        value: Float,
        textPaintInstance: Paint,
        lineY: Float
    ) {
        val displayedText = "${String.format("%.02f", value)} м"
        val textBounds = Rect()
        textPaintInstance.getTextBounds(displayedText, 0, displayedText.length, textBounds)

        val textWidth = textPaintInstance.measureText(displayedText)
        val textHeight = textBounds.height().toFloat()

        val textX = (width.toFloat() - textWidth) / 2
        val textY = lineY - medianTextInset

        drawTextBorder(
            startTextX = textX,
            startTextY = textY,
            textHeight = textHeight,
            textWidth = textWidth
        )

        drawText(
            displayedText,
            textX,
            textY,
            textPaintInstance
        )
    }

    private fun Canvas.drawTextBorder(
        startTextX: Float,
        startTextY: Float,
        textHeight: Float,
        textWidth: Float
    ) {
        val borderTextRect = RectF(
            startTextX - textBorderInset,
            startTextY - textHeight - textBorderInset,
            startTextX + textWidth + textBorderInset,
            startTextY + textBorderInset
        )

        drawRoundRect(
            borderTextRect,
            textBorderCornerRadius,
            textBorderCornerRadius,
            textBackgroundPaint
        )
    }

    /**
     * Work with calendar methods
     */
    private fun isSameDay(
        firstCalendar: Calendar?,
        secondCalendar: Calendar
    ): Boolean {
        return firstCalendar?.get(Calendar.YEAR) == secondCalendar.get(Calendar.YEAR) &&
                firstCalendar.get(Calendar.DAY_OF_YEAR) == secondCalendar.get(Calendar.DAY_OF_YEAR)
    }

    private fun Calendar.getCalendarInstanceByTargetIndex(index: Int): Calendar {
        return this.apply {
            set(
                Calendar.DAY_OF_YEAR,
                currentCalendar.get(Calendar.DAY_OF_YEAR) - (dayCount - 1) + index
            )
        }
    }

    private fun Calendar.clearTime() {
        set(Calendar.AM_PM, Calendar.AM)
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    /**
     * Save and restore state methods
     */
    override fun onSaveInstanceState(): Parcelable {
        return SavedState(super.onSaveInstanceState())
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        when (state) {
            is SavedState -> {
                super.onRestoreInstanceState(state.superState)
                chartData = state.races
                dayCount = state.chartDayCount
            }

            else -> super.onRestoreInstanceState(state)
        }
    }

    private inner class SavedState : BaseSavedState {
        var races = emptyList<Stats>()
        var chartDayCount = 0
        var currentScale = 0f

        constructor(source: Parcelable?) : super(source) {
            races = chartData
            chartDayCount = dayCount
        }

        constructor(source: Parcel?) : super(source) {
            source?.readParcelList(races)
            chartDayCount = source?.readInt() ?: 0
            currentScale = source?.readFloat() ?: 0f
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeList(races)
            out.writeInt(chartDayCount)
            out.writeFloat(currentScale)
        }

        @JvmField
        val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

            override fun createFromParcel(source: Parcel?): SavedState {
                return SavedState(source)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}