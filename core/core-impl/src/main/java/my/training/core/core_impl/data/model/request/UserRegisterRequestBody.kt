package my.training.core.core_impl.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserRegisterRequestBody(
    @SerialName("data")
    val registerData: RegisterDataRequestBody,
    @SerialName("deviceInfo")
    val deviceInfo: DeviceInfoRequestBody
)