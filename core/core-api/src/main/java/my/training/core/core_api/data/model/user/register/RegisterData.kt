package my.training.core.core_api.data.model.user.register

data class RegisterData(
    val login: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val email: String
)