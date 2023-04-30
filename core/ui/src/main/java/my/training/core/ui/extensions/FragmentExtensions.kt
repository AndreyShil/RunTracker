package my.training.core.ui.extensions

import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment

@ColorInt
fun Fragment.getAttrColor(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    requireActivity().theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}

fun Fragment.showSnackbar(message: String) {
    requireView().showSnackbar(message)
}

fun Fragment.hideKeyboard() {
    val inputMethodManager = requireActivity().getSystemService(InputMethodManager::class.java)
    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
}