package my.training.feature.tracker.presentation.races.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import my.training.core.core_api.data.model.race.Race
import my.training.feature.tracker.databinding.ItemRaceBinding

internal class RacesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding by viewBinding(ItemRaceBinding::bind)

    fun bind(race: Race) {
        binding.run {
            tvDistance.text = race.distance.toString()
            tvTime.text = race.duration.toString()
        }
    }

}