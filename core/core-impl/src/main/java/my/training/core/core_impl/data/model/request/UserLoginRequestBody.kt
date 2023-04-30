package my.training.core.core_impl.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequestBody(
    val login: String,
    val password: String
)