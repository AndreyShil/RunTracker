package my.training.core.core_impl.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserLoginRequestBody(
    @SerialName("data")
    val loginData: LoginDataRequestBody,
    @SerialName("deviceInfo")
    val deviceInfo: DeviceInfoRequestBody
)