package my.training.core.core_impl.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterRequestBody(
    val login: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val email: String
)