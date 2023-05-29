package my.training.core.core_api.data.model.response.race

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
    @SerialName("mapScreen")
    val mapScreen: String?,
    @SerialName("averageSpeed")
    val averageSpeed: Double?,
    @SerialName("burnedCalories")
    val burnedCalories: Double?
)