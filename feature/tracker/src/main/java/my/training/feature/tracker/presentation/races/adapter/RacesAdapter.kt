package my.training.feature.tracker.presentation.races.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import my.training.core.core_api.domain.manager.FirebaseStorageManager
import my.training.feature.tracker.R
import my.training.feature.tracker.data.RaceModel
import javax.inject.Inject

internal typealias RaceListener = (String) -> Unit

internal class RacesAdapter @Inject constructor(
    private val firebaseStorageManager: FirebaseStorageManager
) : ListAdapter<RaceModel, RacesViewHolder>(RacesDiffUtil()) {

    private var callback: RaceListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RacesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RacesViewHolder(
            view = inflater.inflate(R.layout.item_race, parent, false),
            clickListener = callback,
            firebaseStorageManager = firebaseStorageManager
        )
    }

    override fun onBindViewHolder(holder: RacesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: RacesViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val payload = payloads.firstOrNull()
        if (payload is RacePayloads.OnCheckedStateChanged) {
            holder.bindAfterExpand(payload.model)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    fun setListener(listener: RaceListener) {
        callback = listener
    }
}