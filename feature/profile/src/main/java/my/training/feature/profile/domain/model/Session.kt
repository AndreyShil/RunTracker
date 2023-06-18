package my.training.feature.profile.domain.model

internal data class Session(
    val id: String,
    val deviceModel: String,
    val deviceType: String,
    val isCurrentSession: Boolean
)