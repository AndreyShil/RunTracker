package my.training.core.core_impl.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RegisterDataRequestBody(
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String,
    @SerialName("firstName")
    val firstName: String,
    @SerialName("lastName")
    val lastName: String,
    @SerialName("email")
    val email: String
)