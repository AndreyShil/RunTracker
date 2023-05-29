package my.training.core.core_api.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceInfoRequestBody(
    @SerialName("deviceId")
    val deviceId: String,
    @SerialName("deviceModel")
    val deviceModel: String,
    @SerialName("deviceType")
    val deviceType: String
)