package my.training.core.ui.custom_view.item_decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import my.training.core.ui.R

class AllDividerItemDecoration(
    context: Context,
    @DrawableRes dividerRes: Int = R.drawable.divider_1
) : ItemDecoration() {

    private val divider by lazy {
        ContextCompat.getDrawable(context, dividerRes)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        parent.children.forEachIndexed { index, child ->
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.top + params.topMargin
            val bottom = top + (divider?.intrinsicHeight ?: 0)
            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)

            if (index == parent.childCount - 1) {
                val lastItemTop = child.bottom + params.bottomMargin
                val lastItemBottom = lastItemTop + (divider?.intrinsicHeight ?: 0)
                divider?.setBounds(left, lastItemTop, right, lastItemBottom)
                divider?.draw(c)
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (divider == null) {
            outRect[0, 0, 0] = 0
            return
        }
        outRect[0, 0, 0] = divider?.intrinsicHeight ?: 0
    }
}

