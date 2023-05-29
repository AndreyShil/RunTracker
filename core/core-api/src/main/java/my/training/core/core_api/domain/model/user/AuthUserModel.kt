package my.training.core.core_api.domain.model.user

data class AuthUserModel(
    val accessToken: String = "",
    val user: User = User()
)