package my.training.core.core_impl.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class DeviceInfoRequestBody(
    @SerialName("deviceId")
    val deviceId: String,
    @SerialName("deviceModel")
    val deviceModel: String,
    @SerialName("deviceType")
    val deviceType: String
)