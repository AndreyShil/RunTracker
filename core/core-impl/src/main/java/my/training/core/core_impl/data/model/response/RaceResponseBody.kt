package my.training.core.core_impl.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RaceResponseBody(
    @SerialName("id")
    val id: String?,
    @SerialName("date")
    val date: String?,
    @SerialName("distance")
    val distance: Int?,
    @SerialName("duration")
    val duration: Long?,
    @SerialName("mapScreenshot")
    val mapScreenshot: String?
)