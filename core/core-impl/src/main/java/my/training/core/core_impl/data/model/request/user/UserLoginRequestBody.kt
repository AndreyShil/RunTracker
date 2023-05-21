package my.training.core.core_impl.data.model.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import my.training.core.core_impl.data.model.request.DeviceInfoRequestBody

@Serializable
internal data class UserLoginRequestBody(
    @SerialName("data")
    val loginData: LoginDataRequestBody,
    @SerialName("deviceInfo")
    val deviceInfo: DeviceInfoRequestBody
)