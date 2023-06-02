package my.training.feature.stats.custom_view.stats_view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import my.training.core.core_api.extensions.readParcelList
import my.training.feature.stats.R
import my.training.feature.stats.model.RaceData
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.math.min
import kotlin.math.roundToInt

private const val CHART_SIZE_RATIO = 0.3f
private const val ANIMATION_DURATION = 1_000L
private const val MAX_ALPHA = 255
private const val DASH_PHASE = 0f
private const val MIN_SCALE_VALUE = 0f
private const val MAX_SCALE_VALUE = 1f
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
        ContextCompat.getColor(context, my.training.core.ui.R.color.md_theme_light_onSurface)
    }

    private val additionalTextColor by lazy(LazyThreadSafetyMode.NONE) {
        ContextCompat.getColor(context, my.training.core.ui.R.color.md_theme_light_onSurfaceVariant)
    }

    private val lightColor by lazy(LazyThreadSafetyMode.NONE) {
        ContextCompat.getColor(context, my.training.core.ui.R.color.md_theme_light_surfaceVariant)
    }

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, my.training.core.ui.R.color.seed)
    }

    private val emptyBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, my.training.core.ui.R.color.seed)
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

    private val textBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
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

    private var chartData: List<RaceData> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private var dayCount = 0
    private var barHeightScale = 0f

    private val currentCalendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        .apply {
            clearTime()
        }

    /**
     * Data setting methods
     */
    fun setStatsData(
        stats: List<RaceData>
    ) {
        chartData = stats
    }

    fun updateDaysCount(dayCount: Int) {
        this.dayCount = dayCount
        getBarHeightScaleAnimator().start()
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

        val desiredSize = if (heightSpecSize >= widthSpecSize) {
            heightSpecSize * CHART_SIZE_RATIO
        } else {
            widthSpecSize * CHART_SIZE_RATIO
        }

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

    /**
     * Drawing methods
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawBarChart()
    }

    private fun Canvas.drawBarChart() {
        var barStartX = defaultPadding
        val startDateRangeCalendar = getCalendarInstanceWithDecreaseDays()
        val startRangeDate = startDateRangeCalendar.time

        val filteredData = chartData
            .filter { it.getDateInstance()?.after(startRangeDate) == true }

        val maxValue = filteredData.maxOf { it.distance }
        val barUnitY = (height / (BAR_HEIGHT_RATIO * maxValue))
        val barWidth = (width - 2 * defaultPadding) / (dayCount * 2 - 1)

        for (index in 0 until dayCount) {
            val targetCalendar = startDateRangeCalendar.getCalendarInstanceByTargetIndex(index)

            val targetRace = filteredData.find { it.getStartDayDate() == targetCalendar.time }
            val calendar = targetRace?.createCalendarInstance()

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
            raceItems = filteredData,
            unitY = barUnitY
        )
    }

    private fun Canvas.drawBars(
        isTargetDay: Boolean,
        raceData: RaceData?,
        barStartX: Float,
        barWidth: Float,
        unitY: Double
    ) {
        if (isTargetDay && raceData != null) {
            drawRoundRect(
                barStartX,
                (height - (raceData.distance * unitY) * barHeightScale).toFloat(),
                barStartX + barWidth,
                height.toFloat(),
                barRadius,
                barRadius,
                barPaint
            )
        } else {
            drawRoundRect(
                barStartX,
                height * (1f - barHeightScale),
                barStartX + barWidth,
                height.toFloat(),
                barRadius,
                barRadius,
                emptyBarPaint
            )
        }
    }

    private fun Canvas.drawMedianInfoLines(
        raceItems: List<RaceData>,
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
        val lineY = (height - medianY * barHeightScale).toFloat()
        val animatedAlpha = (MAX_ALPHA * barHeightScale).roundToInt()

        drawLine(
            0f,
            lineY,
            width.toFloat(),
            lineY,
            linePaintInstance.apply {
                alpha = animatedAlpha
            }
        )

        drawLineText(
            value = value,
            textPaintInstance = textPaintInstance,
            animatedAlpha = animatedAlpha,
            lineY = lineY
        )
    }

    private fun Canvas.drawLineText(
        value: Float,
        textPaintInstance: Paint,
        animatedAlpha: Int,
        lineY: Float
    ) {
        val displayedText = "${String.format("%.02f", value)} м"
        val textBounds = Rect()
        textPaintInstance.getTextBounds(displayedText, 0, displayedText.length, textBounds)

        textPaintInstance.alpha = animatedAlpha
        val textWidth = textPaintInstance.measureText(displayedText)
        val textHeight = textBounds.height().toFloat()

        val textX = (width.toFloat() - textWidth) / 2
        val textY = lineY - medianTextInset

        drawTextBorder(
            startTextX = textX,
            startTextY = textY,
            animatedAlpha = animatedAlpha,
            textHeight = textHeight,
            textWidth = textWidth
        )

        drawText(
            displayedText,
            textX,
            textY,
            textPaintInstance.apply {
                alpha = animatedAlpha
            }
        )
    }

    private fun Canvas.drawTextBorder(
        startTextX: Float,
        startTextY: Float,
        animatedAlpha: Int,
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
            textBorderPaint.apply {
                alpha = animatedAlpha
            }
        )
    }

    private fun getBarHeightScaleAnimator(): ValueAnimator {
        return ValueAnimator.ofFloat(MIN_SCALE_VALUE, MAX_SCALE_VALUE)
            .apply {
                duration = ANIMATION_DURATION
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener {
                    barHeightScale = it.animatedValue as Float
                    invalidate()
                }
            }
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

    private fun RaceData.createCalendarInstance(): Calendar? {
        return getDateInstance()?.let { date ->
            Calendar.getInstance().apply {
                time = date
            }
        }
    }

    private fun Calendar.getCalendarInstanceByTargetIndex(index: Int): Calendar {
        return this.apply {
            set(
                Calendar.DAY_OF_YEAR,
                currentCalendar.get(Calendar.DAY_OF_YEAR) - (dayCount - 1) + index
            )
        }
    }

    private fun getCalendarInstanceWithDecreaseDays(): Calendar {
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
                barHeightScale = state.currentScale
            }

            else -> super.onRestoreInstanceState(state)
        }
    }

    private inner class SavedState : BaseSavedState {
        var races = emptyList<RaceData>()
        var chartDayCount = 0
        var currentScale = 0f

        constructor(source: Parcelable?) : super(source) {
            races = chartData
            chartDayCount = dayCount
            currentScale = barHeightScale
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