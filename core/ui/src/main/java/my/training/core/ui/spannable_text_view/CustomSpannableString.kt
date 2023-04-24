package my.training.core.ui.spannable_text_view

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import androidx.annotation.ColorInt

class CustomSpannableString(
    private val context: Context,
    private val text: String = "",
    @ColorInt private val textColorNormal: Int = Color.BLACK,
    @ColorInt private val textColorPressed: Int = Color.LTGRAY,
    @ColorInt private val backgroundColorNormal: Int = Color.TRANSPARENT,
    @ColorInt private val backgroundColorPressed: Int = Color.TRANSPARENT
) : SpannableString(text) {

    var spannableDataList: MutableList<SpannableData> = mutableListOf()

    fun clear(): CustomSpannableString {
        spannableDataList.clear()
        return this
    }

    fun append(
        text: String = "",
        textColorNormal: Int = Color.BLACK,
        textColorPressed: Int = Color.LTGRAY,
        backgroundColorNormal: Int = Color.TRANSPARENT,
        backgroundColorPressed: Int = Color.TRANSPARENT,
        onClick: (() -> Unit)? = null
    ): CustomSpannableString {
        spannableDataList.add(
            SpannableData(
                text = text,
                textColorNormal = textColorNormal,
                textColorPressed = textColorPressed,
                backgroundColorNormal = backgroundColorNormal,
                backgroundColorPressed = backgroundColorPressed,
                onClick = onClick,
            )
        )
        return this
    }

    fun appendData(spannableDataList: List<SpannableData>): CustomSpannableString {
        this.spannableDataList.addAll(spannableDataList)
        return this
    }

    fun build(): CustomSpannableString {
        var resultedText = ""
        spannableDataList.map { resultedText += it.text }

        val spannableString =
            CustomSpannableString(
                context = this.context,
                text = resultedText,
                textColorNormal = this.textColorNormal,
                textColorPressed = this.textColorPressed,
                backgroundColorNormal = this.backgroundColorNormal,
                backgroundColorPressed = this.backgroundColorPressed,
            )

        spannableString.spannableDataList = spannableDataList

        var startIndex = 0
        for (span in spannableDataList) {
            val mText = span.text
            val textColorNormal = span.textColorNormal
            val backgroundColorNormal = span.backgroundColorNormal
            val textColorPressed = span.textColorPressed
            val backgroundColorPressed = span.backgroundColorPressed
            spannableString.setSpan(
                TouchableSpan(
                    onClick = span.onClick,
                    normalTextColor = textColorNormal,
                    normalBackgroundColor = backgroundColorNormal,
                    pressedTextColor = textColorPressed,
                    pressedBackgroundColor = backgroundColorPressed,
                ),
                startIndex,
                startIndex + mText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            startIndex += mText.length
        }
        return spannableString
    }
}