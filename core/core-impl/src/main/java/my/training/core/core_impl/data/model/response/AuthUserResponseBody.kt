package my.training.core.core_impl.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AuthUserResponseBody(
    @SerialName("token")
    val token: String?,
    @SerialName("profile")
    val user: UserResponseBody?
)
