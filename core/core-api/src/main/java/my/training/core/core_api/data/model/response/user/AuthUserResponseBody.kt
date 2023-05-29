package my.training.core.core_api.data.model.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthUserResponseBody(
    @SerialName("token")
    val token: String?,
    @SerialName("profile")
    val user: UserResponseBody?
)
