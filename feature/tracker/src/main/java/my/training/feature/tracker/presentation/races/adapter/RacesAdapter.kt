package my.training.feature.tracker.presentation.races.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.training.core.core_api.domain.manager.FirebaseStorageManager
import my.training.feature.tracker.R
import my.training.feature.tracker.domain.model.RaceModel
import javax.inject.Inject

internal typealias RaceListener = (String) -> Unit

internal class RacesAdapter @Inject constructor(
    private val firebaseStorageManager: FirebaseStorageManager
) : ListAdapter<RaceModel, RecyclerView.ViewHolder>(RacesDiffUtil()) {

    private var callback: RaceListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            RaceModel.DATE_HEADER_VIEW_TYPE -> {
                DateHeaderViewHolder(
                    inflater.inflate(R.layout.item_date_header, parent, false)
                )
            }

            RaceModel.RACE_VIEW_TYPE -> {
                RacesViewHolder(
                    view = inflater.inflate(R.layout.item_race, parent, false),
                    clickListener = callback,
                    firebaseStorageManager = firebaseStorageManager
                )
            }

            else -> throw IllegalArgumentException("Invalid races adapter view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is DateHeaderViewHolder -> holder.bind(item)
            is RacesViewHolder -> holder.bind(item)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (holder is RacesViewHolder) {
            val payload = payloads.firstOrNull()
            if (payload is RacePayloads.OnCheckedStateChanged) {
                holder.bindAfterExpand(payload.model)
            } else {
                super.onBindViewHolder(holder, position, payloads)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getItemViewType()
    }

    fun setListener(listener: RaceListener) {
        callback = listener
    }
}