package my.training.core.core_impl.data.model.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserResponseBody(
    @SerialName("login")
    val login: String?,
    @SerialName("firstName")
    val firstName: String?,
    @SerialName("lastName")
    val lastName: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("photo")
    val photo: String?
)
