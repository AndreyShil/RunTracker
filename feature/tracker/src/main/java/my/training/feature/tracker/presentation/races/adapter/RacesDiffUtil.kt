package my.training.feature.tracker.presentation.races.adapter

import androidx.recyclerview.widget.DiffUtil
import my.training.feature.tracker.data.RaceModel

internal class RacesDiffUtil : DiffUtil.ItemCallback<RaceModel>() {

    override fun areItemsTheSame(oldItem: RaceModel, newItem: RaceModel): Boolean {
        return oldItem.race.id == newItem.race.id
    }

    override fun areContentsTheSame(oldItem: RaceModel, newItem: RaceModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: RaceModel, newItem: RaceModel): Any? {
        if (oldItem.race == newItem.race) {
            return RacePayloads.OnCheckedStateChanged(newItem)
        }
        return super.getChangePayload(oldItem, newItem)
    }
}