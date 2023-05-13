package my.training.feature.tracker.presentation.races.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import my.training.core.core_api.data.model.race.Race
import my.training.feature.tracker.R
import javax.inject.Inject

internal class RacesAdapter @Inject constructor() :
    ListAdapter<Race, RacesViewHolder>(RacesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RacesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RacesViewHolder(inflater.inflate(R.layout.item_race, parent, false))
    }

    override fun onBindViewHolder(holder: RacesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}