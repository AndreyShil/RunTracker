package my.training.feature.profile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import my.training.feature.profile.R
import my.training.feature.profile.domain.model.Session
import javax.inject.Inject

internal typealias RemoveSessionListener = (String) -> Unit

internal class SessionsAdapter @Inject constructor() :
    ListAdapter<Session, SessionsViewHolder>(SessionsDiffUtil()) {

    private var listener: RemoveSessionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SessionsViewHolder(
            view = inflater.inflate(R.layout.item_session, parent, false),
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: SessionsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setListener(listener: RemoveSessionListener) {
        this.listener = listener
    }
}