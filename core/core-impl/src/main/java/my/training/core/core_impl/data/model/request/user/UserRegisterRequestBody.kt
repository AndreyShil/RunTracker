package my.training.core.core_impl.data.model.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import my.training.core.core_impl.data.model.request.DeviceInfoRequestBody

@Serializable
internal data class UserRegisterRequestBody(
    @SerialName("data")
    val registerData: RegisterDataRequestBody,
    @SerialName("deviceInfo")
    val deviceInfo: DeviceInfoRequestBody
)