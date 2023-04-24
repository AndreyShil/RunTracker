package my.training.core.ui.spannable_text_view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class SpannableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private var customSpannableString: CustomSpannableString? = null

    init {
        customSpannableString = CustomSpannableString(context)
        this.highlightColor = Color.TRANSPARENT
    }

    fun getEmptySpannableString(): CustomSpannableString =
        customSpannableString?.clear() ?: CustomSpannableString(context)

    fun setSpannableText(spannableString: CustomSpannableString?) {
        this.text = spannableString
        spannableString?.spannableDataList?.map { spannableData ->
            if (spannableData.onClick != null) {
                makeTextViewClickable()
                return
            }
        }
    }

    private fun makeTextViewClickable() {
        this.movementMethod = LinkTouchMovementMethod()
    }

}