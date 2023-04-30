package my.training.core.ui.custom_view.spannable_text_view

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

internal class TouchableSpan(
    private var onClick: (() -> Unit)? = null,
    private val normalTextColor: Int,
    private val pressedTextColor: Int,
    private val normalBackgroundColor: Int,
    private val pressedBackgroundColor: Int
) : ClickableSpan() {

    private var mIsPressed = false

    fun setPressed(isSelected: Boolean) {
        mIsPressed = isSelected
    }

    override fun onClick(widget: View) {
        onClick?.invoke()
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = if (mIsPressed && onClick != null) pressedTextColor else normalTextColor
        ds.bgColor =
            if (mIsPressed && onClick != null) pressedBackgroundColor else normalBackgroundColor
        ds.isUnderlineText = false
    }

}