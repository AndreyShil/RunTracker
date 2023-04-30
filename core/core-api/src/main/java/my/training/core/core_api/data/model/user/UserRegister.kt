package my.training.core.core_api.data.model.user

data class UserRegister(
    val login: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val email: String
)