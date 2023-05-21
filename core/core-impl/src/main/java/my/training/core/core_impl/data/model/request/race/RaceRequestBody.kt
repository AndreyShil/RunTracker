package my.training.core.core_impl.data.model.request.race

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RaceRequestBody(
    @SerialName("date")
    val date: String,
    @SerialName("distance")
    val distance: Int,
    @SerialName("duration")
    val duration: Long,
    @SerialName("mapScreen")
    val mapScreen: String?
)