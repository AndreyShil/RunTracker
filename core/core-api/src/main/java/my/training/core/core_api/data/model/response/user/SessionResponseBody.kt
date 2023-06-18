package my.training.core.core_api.data.model.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionResponseBody(
    @SerialName("id")
    val id: String?,
    @SerialName("deviceModel")
    val deviceModel: String?,
    @SerialName("deviceType")
    val deviceType: String?,
    @SerialName("isCurrentSession")
    val isCurrentSession: Boolean?
)