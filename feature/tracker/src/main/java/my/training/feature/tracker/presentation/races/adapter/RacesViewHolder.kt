package my.training.feature.tracker.presentation.races.adapter

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import coil.load
import my.training.core.core_api.domain.manager.FirebaseStorageManager
import my.training.feature.tracker.R
import my.training.feature.tracker.domain.model.RaceModel
import my.training.feature.tracker.databinding.ItemRaceBinding
import my.training.feature.tracker.extension.displayCalories
import my.training.feature.tracker.extension.displayDistance
import my.training.feature.tracker.extension.displaySpeed
import my.training.feature.tracker.extension.getFormattedWatchTime

private const val ANIMATION_DURATION = 1000L
private const val START_GUIDELINE_POSITION = 0f
private const val MIDDLE_GUIDELINE_POSITION = 0.5f

internal class RacesViewHolder(
    view: View,
    private val clickListener: RaceListener?,
    private val firebaseStorageManager: FirebaseStorageManager
) : RecyclerView.ViewHolder(view) {

    private val binding by viewBinding(ItemRaceBinding::bind)

    private val expandedHeight by lazy(LazyThreadSafetyMode.NONE) {
        view.resources.getDimensionPixelSize(R.dimen.expanded_card_height)
    }

    private val collapsedHeight by lazy(LazyThreadSafetyMode.NONE) {
        view.resources.getDimensionPixelSize(R.dimen.collapsed_card_height)
    }

    fun bind(model: RaceModel) {
        require(model is RaceModel.RaceInfo)

        initView(model)
        initRootClickListener(model)
        loadImage(model.race.mapScreen)
    }

    fun bindAfterExpand(model: RaceModel) {
        require(model is RaceModel.RaceInfo)
        initRootClickListener(model)
    }

    private fun initView(model: RaceModel.RaceInfo) {
        val layoutHeight = if (model.isExpanded) expandedHeight else collapsedHeight
        val guideLinePosition =
            if (model.isExpanded) START_GUIDELINE_POSITION else MIDDLE_GUIDELINE_POSITION

        binding.run {
            tvDistance.text = model.race.distance.displayDistance()
            tvTime.text = (model.race.duration * 1000).getFormattedWatchTime()
            tvAverageSpeed.text = model.race.averageSpeed.displaySpeed()
            tvCalories.text = model.race.burnedCalories.displayCalories()
            guidelineDynamic.setGuidelinePercent(guideLinePosition)
            setInfoBackground(model.isExpanded)
        }

        updateLayoutHeight(layoutHeight)
    }

    private fun initRootClickListener(model: RaceModel.RaceInfo) {
        binding.root.setOnClickListener {
            binding.root.isClickable = false
            if (model.isExpanded) {
                getAnimatorSetForExpandedItem(model.race.id).start()
            } else {
                getAnimatorSetForCollapsedItem(model.race.id).start()
            }
        }
    }

    private fun getAnimatorSetForCollapsedItem(raceId: String): AnimatorSet {
        return getAnimatorSet(
            raceId = raceId,
            isExpanded = true
        ).apply {
            playTogether(
                getLayoutHeightAnimator(collapsedHeight, expandedHeight),
                getGuideLinePositionAnimator(MIDDLE_GUIDELINE_POSITION, START_GUIDELINE_POSITION)
            )
        }
    }

    private fun getAnimatorSetForExpandedItem(raceId: String): AnimatorSet {
        return getAnimatorSet(
            raceId = raceId,
            isExpanded = false
        ).apply {
            playTogether(
                getLayoutHeightAnimator(expandedHeight, collapsedHeight),
                getGuideLinePositionAnimator(START_GUIDELINE_POSITION, MIDDLE_GUIDELINE_POSITION)
            )
        }
    }

    private fun getAnimatorSet(
        raceId: String,
        isExpanded: Boolean
    ): AnimatorSet {
        return AnimatorSet().apply {
            addListener(
                onEnd = {
                    binding.root.isClickable = true
                    setInfoBackground(isExpanded)
                    clickListener?.invoke(raceId)
                }
            )
        }
    }

    private fun getLayoutHeightAnimator(vararg values: Int): ValueAnimator {
        return ValueAnimator.ofInt(*values).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = ANIMATION_DURATION
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                updateLayoutHeight(value)
            }
        }
    }

    private fun getGuideLinePositionAnimator(vararg values: Float): ValueAnimator {
        return ValueAnimator.ofFloat(*values).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = ANIMATION_DURATION
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Float
                binding.guidelineDynamic.setGuidelinePercent(value)
            }
        }
    }

    private fun updateLayoutHeight(height: Int) {
        val layoutParams: ViewGroup.LayoutParams = binding.contentLayout.layoutParams
        layoutParams.height = height
        binding.contentLayout.layoutParams = layoutParams
    }

    private fun setInfoBackground(isExpanded: Boolean) {
        if (!isExpanded) {
            binding.infoLayout.background = null
            return
        }
        binding.infoLayout.background = ContextCompat.getDrawable(
            itemView.context,
            my.training.core.ui.R.drawable.bg_rounded_16
        )
    }

    private fun loadImage(imageUrl: String?) {
        imageUrl ?: return
        firebaseStorageManager.downloadImage(imageUrl) {
            val imageLoader = ImageLoader.Builder(itemView.context)
                .crossfade(true)
                .build()

            binding.ivMap.load(
                data = it,
                imageLoader = imageLoader
            )
        }
    }
}