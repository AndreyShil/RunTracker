package my.training.core.core_impl.data.model.response

import kotlinx.serialization.Serializable

@Serializable
internal data class AuthUserResponseBody(
    val token: String?,
    val user: UserResponseBody?
)
