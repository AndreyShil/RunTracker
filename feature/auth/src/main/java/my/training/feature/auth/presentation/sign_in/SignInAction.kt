package my.training.feature.auth.presentation.sign_in

internal sealed interface SignInAction {

    data class OnLoginChanged(val inputLogin: String) : SignInAction
    data class OnPasswordChanged(val inputPassword: String) : SignInAction
    object DoLoginRequest : SignInAction

}