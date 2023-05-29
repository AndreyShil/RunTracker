package my.training.core.core_api.data.model.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginDataRequestBody(
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String
)