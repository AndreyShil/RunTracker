package my.training.feature.profile.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import my.training.feature.profile.domain.model.Session

internal class SessionsDiffUtil : DiffUtil.ItemCallback<Session>() {

    override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
        return oldItem.id == newItem.id
    }
}
