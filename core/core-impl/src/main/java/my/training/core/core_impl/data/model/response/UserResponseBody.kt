package my.training.core.core_impl.data.model.response

import kotlinx.serialization.Serializable

@Serializable
internal data class UserResponseBody(
    val login: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val photo: String?
)
