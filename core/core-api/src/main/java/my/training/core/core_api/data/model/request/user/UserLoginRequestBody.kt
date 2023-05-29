package my.training.core.core_api.data.model.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import my.training.core.core_api.data.model.request.DeviceInfoRequestBody

@Serializable
data class UserLoginRequestBody(
    @SerialName("data")
    val loginData: LoginDataRequestBody,
    @SerialName("deviceInfo")
    val deviceInfo: DeviceInfoRequestBody
)