package my.training.feature.tracker.presentation.tracker

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.Scene
import androidx.transition.Transition
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.button.MaterialButton
import my.training.feature.tracker.R
import my.training.feature.tracker.databinding.ViewControlBinding
import my.training.feature.tracker.domain.model.RunningState

private const val TRANSITION_DURATION = 300L

internal class ControlView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding by viewBinding(ViewControlBinding::bind)

    private val startScene by lazy(LazyThreadSafetyMode.NONE) {
        Scene.getSceneForLayout(
            binding.root as ViewGroup,
            R.layout.scene_start_control_buttons,
            context
        )
    }

    private val intermediateScene by lazy(LazyThreadSafetyMode.NONE) {
        Scene.getSceneForLayout(
            binding.root as ViewGroup,
            R.layout.scene_intermediate_control_buttons,
            context
        )
    }

    private val finishScene by lazy(LazyThreadSafetyMode.NONE) {
        Scene.getSceneForLayout(
            binding.root as ViewGroup,
            R.layout.scene_finish_control_buttons,
            context
        )
    }

    private var controlListener: ControlListener? = null

    private val transitionSet = TransitionSet().apply {
        addTransition(Fade())
        addTransition(ChangeBounds())
        ordering = TransitionSet.ORDERING_TOGETHER
        duration = TRANSITION_DURATION
        interpolator = AccelerateInterpolator()

        addListener(
            object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition) = Unit
                override fun onTransitionEnd(transition: Transition) {
                    updateButtonsListeners()
                }

                override fun onTransitionCancel(transition: Transition) = Unit
                override fun onTransitionPause(transition: Transition) = Unit
                override fun onTransitionResume(transition: Transition) = Unit
            }
        )
    }

    init {
        inflate(context, R.layout.view_control, this)
        updateButtonsListeners()
    }

    private fun updateButtonsListeners() {
        findViewById<MaterialButton>(R.id.btn_start)?.setOnClickListener {
            setState(RunningState.IN_PROGRESS)
            controlListener?.onStartClicked()
        }
        findViewById<MaterialButton>(R.id.btn_pause)?.setOnClickListener {
            setState(RunningState.ON_PAUSE)
            controlListener?.onPauseClicked()
        }
        findViewById<MaterialButton>(R.id.btn_stop)?.setOnClickListener {
            setState(RunningState.FINISH)
            controlListener?.onStopClicked()
        }
        findViewById<MaterialButton>(R.id.btn_finish)?.setOnClickListener {
            setState(RunningState.INITIAL)
            controlListener?.onFinishClicked()
        }
    }

    fun setState(state: RunningState) {
        when (state) {
            RunningState.INITIAL -> {
                TransitionManager.go(startScene, transitionSet)
            }

            RunningState.IN_PROGRESS -> {
                TransitionManager.go(intermediateScene, transitionSet)
            }

            RunningState.ON_PAUSE -> {
                TransitionManager.go(intermediateScene, transitionSet)
                findViewById<MaterialButton>(R.id.btn_start).isVisible = true
                findViewById<MaterialButton>(R.id.btn_pause).isVisible = false
            }

            RunningState.FINISH -> {
                TransitionManager.go(finishScene, transitionSet)
            }
        }
    }

    fun setListener(listener: ControlListener) {
        controlListener = listener
    }

    interface ControlListener {
        fun onStartClicked()
        fun onPauseClicked()
        fun onStopClicked()
        fun onFinishClicked()
    }
}
