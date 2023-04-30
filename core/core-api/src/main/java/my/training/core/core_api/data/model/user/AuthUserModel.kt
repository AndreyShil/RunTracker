package my.training.core.core_api.data.model.user

data class AuthUserModel(
    val accessToken: String = "",
    val user: User = User()
)