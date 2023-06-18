package my.training.feature.profile.presentation.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import my.training.feature.profile.databinding.ItemSessionBinding
import my.training.feature.profile.domain.model.Session

internal class SessionsViewHolder(
    view: View,
    private val listener: RemoveSessionListener?
) : RecyclerView.ViewHolder(view) {

    private val binding by viewBinding(ItemSessionBinding::bind)

    fun bind(session: Session) {
        binding.run {
            tvCurrentSession.isVisible = session.isCurrentSession
            tvDeviceModel.text = session.deviceModel
            tvDeviceType.text = session.deviceType
            btnClear.apply {
                isVisible = !session.isCurrentSession
                setOnClickListener { listener?.invoke(session.id) }
            }
        }
    }
}
