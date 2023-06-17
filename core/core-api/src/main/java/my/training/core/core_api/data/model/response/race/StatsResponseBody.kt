package my.training.core.core_api.data.model.response.race

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatsResponseBody(
    @SerialName("date")
    val date: String,
    @SerialName("distance")
    val distance: Int,
    @SerialName("burnedCalories")
    val burnedCalories: Double,
    @SerialName("averageSpeed")
    val averageSpeed: Double
)