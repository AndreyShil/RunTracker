package my.training.feature.tracker.presentation.races.adapter

import androidx.recyclerview.widget.DiffUtil
import my.training.core.core_api.data.model.race.Race

internal class RacesDiffUtil : DiffUtil.ItemCallback<Race>() {

    override fun areItemsTheSame(oldItem: Race, newItem: Race): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Race, newItem: Race): Boolean {
        return oldItem == newItem
    }
}