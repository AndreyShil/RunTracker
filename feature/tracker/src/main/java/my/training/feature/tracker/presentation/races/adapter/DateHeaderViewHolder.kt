package my.training.feature.tracker.presentation.races.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import my.training.core.core_api.extensions.getDisplayedDate
import my.training.feature.tracker.databinding.ItemDateHeaderBinding
import my.training.feature.tracker.data.model.RaceModel

internal class DateHeaderViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    private val binding by viewBinding(ItemDateHeaderBinding::bind)

    fun bind(model: RaceModel) {
        require(model is RaceModel.DateHeader)

        binding.tvDate.text = model.date.getDisplayedDate()
    }
}