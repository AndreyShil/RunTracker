package my.training.core.core_impl.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LoginDataRequestBody(
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String
)