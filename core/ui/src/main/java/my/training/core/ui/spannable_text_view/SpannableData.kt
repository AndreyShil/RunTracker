package my.training.core.ui.spannable_text_view

import android.graphics.Color
import androidx.annotation.ColorInt

data class SpannableData(
    val text: String = "",
    @ColorInt val textColorNormal: Int = Color.BLACK,
    @ColorInt val textColorPressed: Int = Color.LTGRAY,
    @ColorInt val backgroundColorNormal: Int = Color.TRANSPARENT,
    @ColorInt val backgroundColorPressed: Int = Color.TRANSPARENT,
    val onClick: (() -> Unit)? = null
)