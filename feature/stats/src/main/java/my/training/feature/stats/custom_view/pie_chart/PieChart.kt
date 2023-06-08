package my.training.feature.stats.custom_view.pie_chart

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextDirectionHeuristics
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import my.training.core.core_api.extensions.readParcelList
import my.training.feature.stats.R
import kotlin.math.min
import kotlin.math.roundToInt

// Процент ширины для отображения текста от общей ширины View
private const val TEXT_WIDTH_PERCENT = 0.4f

// Процент ширины для отображения круговой диаграммы от общей ширины View
private const val CIRCLE_WIDTH_PERCENT = 0.5f
private const val ANIMATION_DURATION = 1_000L
private const val START_CIRCLE_ANGLE = 0
private const val END_CIRCLE_ANGLE = 360
private const val MIN_PERCENT = 0
private const val MAX_PERCENT = 100

internal class PieChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val circleStrokeWidth by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(R.dimen.pie_chart_stroke_width)
    }

    private val circleSectionSpace by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(R.dimen.pie_chart_circle_space)
    }

    private val smallMargin by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(my.training.core.ui.R.dimen.padding_xxxs)
    }

    private val legendMarginBetweenRows by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(my.training.core.ui.R.dimen.padding_xs)
    }

    private val legendCircleMargin by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(R.dimen.legend_circle_margin)
    }

    private val legendCircleRadius by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(R.dimen.legend_circle_radius)
    }

    private val legendValueTextSize by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(my.training.core.ui.R.dimen.text_size_md)
    }

    private val legendDescriptionTextSize by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(my.training.core.ui.R.dimen.text_size_sm)
    }

    private val totalTextSize by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(my.training.core.ui.R.dimen.text_size_lg)
    }

    private val additionalTotalTextSize by lazy(LazyThreadSafetyMode.NONE) {
        resources.getDimension(my.training.core.ui.R.dimen.text_size_md)
    }

    private val mainTextColor by lazy(LazyThreadSafetyMode.NONE) {
        ContextCompat.getColor(context, R.color.main_text_color)
    }

    private val descriptionTextColor by lazy(LazyThreadSafetyMode.NONE) {
        ContextCompat.getColor(context, R.color.secondary_text_color)
    }

    private val pieChartColors by lazy(LazyThreadSafetyMode.NONE) {
        listOf(
            ContextCompat.getColor(context, R.color.pie_chart_first_color),
            ContextCompat.getColor(context, R.color.pie_chart_second_color),
            ContextCompat.getColor(context, R.color.pie_chart_third_color),
            ContextCompat.getColor(context, R.color.pie_chart_fourth_color)
        )
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = circleStrokeWidth
        isDither = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val legendValueTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = mainTextColor
        textSize = legendValueTextSize
    }

    private val legendDescriptionTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = descriptionTextColor
        textSize = legendDescriptionTextSize
    }

    private val totalTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = mainTextColor
        textSize = totalTextSize
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val additionalTotalTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = descriptionTextColor
        textSize = additionalTotalTextSize
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private var circleRect = RectF()
    private var circleRadius = 0f
    private var circleCenterX = 0f
    private var circleCenterY = 0f

    private var legendTextStartX = 0f
    private var legendTextStartY = 0f

    private var legendHeight: Int = 0

    private var totalTextX = 0f
    private var totalTextY = 0f
    private var totalAmount = 0f
    private var totalSuffix = ""
    private var totalMedianValue: Pair<Double, Double>? = Pair(0.0, 0.0)
    private var additionalTotalTextX = 0f
    private var additionalTotalTextY = 0f

    private var percentageCircleList: List<PieChartModel> = emptyList()
    private val textRowList: MutableList<StaticLayout> = mutableListOf()

    private var dataList: List<Pair<Int, String>> = emptyList()
    private var animationSweepAngle = 0

    fun setDataChart(
        data: List<Pair<Int, String>>,
        totalSuffix: String,
        medianValues: Pair<Double, Double>? = null
    ) {
        requestLayout()
        dataList = data
        this.totalSuffix = totalSuffix
        totalMedianValue = medianValues
        calculatePercentageOfData()
        startAnimation()
    }

    /**
     * Метод жизненного цикла View.
     * Расчет необходимой ширины и высоты View.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textRowList.clear()

        val desiredSize = calculateDesiredSize(
            widthSpecSize = MeasureSpec.getSize(widthMeasureSpec),
            heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        )

        val targetWidth = getMeasuredDimension(
            measureSpec = widthMeasureSpec,
            desiredSize = desiredSize
        )

        val legendWidth = targetWidth * TEXT_WIDTH_PERCENT
        val desiredCircleWidth = targetWidth * CIRCLE_WIDTH_PERCENT

        val targetHeight = calculateViewHeight(
            heightMeasureSpec = heightMeasureSpec,
            legendWidth = legendWidth.roundToInt(),
            desiredSize = desiredSize,
            desiredCircleWidth = desiredCircleWidth
        )

        legendTextStartX = targetWidth - legendWidth
        legendTextStartY = targetHeight.toFloat() / 2 - legendHeight / 2

        calculateCircleRadius(desiredCircleWidth, targetHeight)
        setMeasuredDimension(targetWidth, targetHeight)
    }

    private fun calculateDesiredSize(
        widthSpecSize: Int,
        heightSpecSize: Int
    ): Int {
        val orientation = resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            heightSpecSize
        } else {
            widthSpecSize
        }
    }

    private fun calculateViewHeight(
        heightMeasureSpec: Int,
        legendWidth: Int,
        desiredSize: Int,
        desiredCircleWidth: Float
    ): Int {
        val initSizeHeight = getMeasuredDimension(heightMeasureSpec, desiredSize)
        val rowTotalMargin = smallMargin + legendMarginBetweenRows
        legendHeight = (dataList.size * rowTotalMargin + getLegendTextHeight(legendWidth)).toInt()

        val textHeightWithPadding = legendHeight + paddingTop + paddingBottom
        val desiredCircleSize = (circleStrokeWidth * 2 + desiredCircleWidth).roundToInt()
        return maxOf(initSizeHeight, textHeightWithPadding, desiredCircleSize)
    }

    private fun getLegendTextHeight(maxWidth: Int): Int {
        var textHeight = 0
        dataList.forEach {
            val textLayoutNumber = getMultilineText(
                text = it.first.toString(),
                textPaint = legendValueTextPaint,
                width = maxWidth
            )
            val textLayoutDescription = getMultilineText(
                text = it.second,
                textPaint = legendDescriptionTextPaint,
                width = maxWidth
            )
            textRowList.apply {
                add(textLayoutNumber)
                add(textLayoutDescription)
            }
            textHeight += textLayoutNumber.height + textLayoutDescription.height
        }

        return textHeight
    }

    private fun getMeasuredDimension(
        measureSpec: Int,
        desiredSize: Int
    ): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> min(desiredSize, specSize)
            else -> desiredSize
        }
    }

    /**
     * Отрисовка всех необходимых компонентов на Canvas.
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawCircle()
        canvas?.drawLegendText()
    }

    private fun Canvas.drawCircle() {
        percentageCircleList.forEachIndexed { index, percent ->
            when {
                animationSweepAngle > (percent.percentToStartAt + percent.percentOfCircle) -> {
                    drawArc(
                        circleRect,
                        percent.percentToStartAt,
                        percent.percentOfCircle,
                        false,
                        circlePaint.apply {
                            color = pieChartColors[index % pieChartColors.size]
                        }
                    )
                }

                animationSweepAngle > percent.percentToStartAt -> {
                    drawArc(
                        circleRect,
                        percent.percentToStartAt,
                        animationSweepAngle - percent.percentToStartAt,
                        false,
                        circlePaint.apply {
                            color = pieChartColors[index % pieChartColors.size]
                        }
                    )
                }
            }
        }
    }

    private fun Canvas.drawLegendText() {
        var targetY = legendTextStartY
        textRowList.forEachIndexed { index, staticLayout ->
            if (index % 2 == 0) {
                val xPosition = legendTextStartX + legendCircleMargin + legendCircleRadius
                staticLayout.draw(this, xPosition, targetY)
                drawCircle(
                    legendTextStartX + legendCircleMargin / 2,
                    targetY + staticLayout.height / 2,
                    legendCircleRadius,
                    Paint().apply {
                        color = pieChartColors[(index / 2) % pieChartColors.size]
                    }
                )
                targetY += staticLayout.height + smallMargin
            } else {
                staticLayout.draw(this, legendTextStartX, targetY)
                targetY += staticLayout.height + legendMarginBetweenRows
            }
        }

        drawText(
            getTotalText(),
            totalTextX,
            totalTextY,
            totalTextPaint
        )
        drawText(
            getAdditionalTotalText(),
            additionalTotalTextX,
            additionalTotalTextY,
            additionalTotalTextPaint
        )
    }

    private fun startAnimation() {
        val animator = ValueAnimator.ofInt(START_CIRCLE_ANGLE, END_CIRCLE_ANGLE).apply {
            duration = ANIMATION_DURATION
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener { valueAnimator ->
                animationSweepAngle = valueAnimator.animatedValue as Int
                invalidate()
            }
        }
        animator.start()
    }

    private fun calculateCircleRadius(desiredCircleWidth: Float, height: Int) {
        circleRadius = if (desiredCircleWidth > height) {
            (height.toFloat()) / 2
        } else {
            desiredCircleWidth / 2
        }

        circleRect = RectF(
            circleStrokeWidth,
            height / 2 - circleRadius,
            circleRadius * 2 + circleStrokeWidth,
            height / 2 + circleRadius
        )

        circleCenterX = circleRadius + circleStrokeWidth
        circleCenterY = (height / 2 + circleRadius + (height / 2 - circleRadius)) / 2

        calculateTotalTextPosition()
    }

    private fun calculateTotalTextPosition() {
        val totalTextRect = Rect()
        val totalText = getTotalText()
        totalTextPaint.getTextBounds(totalText, 0, totalText.length, totalTextRect)

        totalTextX = circleCenterX - totalTextRect.width() / 2
        totalTextY = circleCenterY - smallMargin

        val additionalTextRect = Rect()
        val additionalText = getAdditionalTotalText()
        additionalTotalTextPaint.getTextBounds(
            additionalText,
            0,
            additionalText.length,
            additionalTextRect
        )
        additionalTotalTextX = circleCenterX - additionalTextRect.width() / 2
        additionalTotalTextY = circleCenterY + additionalTextRect.height() + smallMargin
    }

    private fun getTotalText(): String {
        return String.format("%.02f", totalAmount) + " $totalSuffix"
    }

    private fun getAdditionalTotalText(): String {
        return String.format("%.02f", totalMedianValue?.second) + " $totalSuffix"
    }

    private fun calculatePercentageOfData() {
        totalAmount = totalMedianValue?.first?.toFloat()
            ?: dataList.fold(0f) { res, value -> res + value.first }

        var startAt = circleSectionSpace
        percentageCircleList = dataList.map { pair ->
            val percent = calculatePercent(pair)
            val resultModel = PieChartModel(
                percentOfCircle = END_CIRCLE_ANGLE * percent / MAX_PERCENT,
                percentToStartAt = startAt.updatePercentStartAt()
            )
            if (percent != 0f)
                startAt += percent + circleSectionSpace
            resultModel
        }
    }

    private fun calculatePercent(pair: Pair<Int, String>): Float {
        val percent = pair.first * MAX_PERCENT / dataList.sumOf { it.first } - circleSectionSpace
        return percent.coerceIn(MIN_PERCENT.toFloat(), MAX_PERCENT.toFloat())
    }

    private fun Float.updatePercentStartAt(): Float {
        var result = this
        if (this < MIN_PERCENT || this > MAX_PERCENT) {
            result = MIN_PERCENT.toFloat()
        }
        return END_CIRCLE_ANGLE * result / MAX_PERCENT
    }

    /**
     * Метод создания [StaticLayout] с переданными значениями
     */
    private fun getMultilineText(
        text: CharSequence,
        textPaint: TextPaint,
        width: Int
    ): StaticLayout {
        return StaticLayout.Builder
            .obtain(text, 0, text.length, textPaint, width)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setTextDirection(TextDirectionHeuristics.LTR)
            .setLineSpacing(0f, 1f)
            .build()
    }

    private fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float) {
        canvas.withTranslation(x, y) {
            draw(this)
        }
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
                dataList = state.data
                animationSweepAngle = state.sweepAngle
            }

            else -> super.onRestoreInstanceState(state)
        }
    }

    private inner class SavedState : BaseSavedState {
        var data = emptyList<Pair<Int, String>>()
        var sweepAngle = 0

        constructor(source: Parcelable?) : super(source) {
            data = dataList
            sweepAngle = animationSweepAngle
        }

        constructor(source: Parcel?) : super(source) {
            source?.readParcelList(data)
            sweepAngle = source?.readInt() ?: 0
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeList(data)
            out.writeInt(sweepAngle)
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