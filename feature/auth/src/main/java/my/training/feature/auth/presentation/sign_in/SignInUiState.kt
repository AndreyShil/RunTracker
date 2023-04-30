package my.training.feature.auth.presentation.sign_in


internal data class SignInUiState(
    val login: String = "",
    val password: String = "",
    val error: Throwable? = null
) {

    fun isValid(): Boolean {
        return login.isNotEmpty() && password.isNotEmpty()
    }
}