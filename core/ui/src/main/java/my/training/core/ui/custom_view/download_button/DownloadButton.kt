package my.training.core.ui.custom_view.download_button

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import my.training.core.ui.R
import my.training.core.ui.databinding.ViewDownloadButtonBinding

class DownloadButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding by viewBinding(ViewDownloadButtonBinding::bind)

    var buttonText: String = ""
        set(value) {
            binding.button.text = value
            field = value
        }

    var buttonEnabled: Boolean = true
        set(value) {
            binding.button.isEnabled = value
            field = value
        }

    init {
        inflate(context, R.layout.view_download_button, this)
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.DownloadButton)

        buttonText = attributes.getString(R.styleable.DownloadButton_android_text) ?: ""
        buttonEnabled = attributes.getBoolean(R.styleable.DownloadButton_android_enabled, true)

        attributes.recycle()
    }

    fun setOnClickListener(callback: (View) -> Unit) {
        binding.button.setOnClickListener(callback)
    }

    fun updateLoadingState(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.button.isVisible = !isLoading
    }

}