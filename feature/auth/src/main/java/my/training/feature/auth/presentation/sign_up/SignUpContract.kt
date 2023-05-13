package my.training.feature.auth.presentation.sign_up

import my.training.core.ui.base.UiEffect
import my.training.core.ui.base.UiEvent
import my.training.core.ui.base.UiState

internal object SignUpContract {

    data class State(
        val login: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val password: String = "",
        val passwordRepeat: String = "",
        val isLoading: Boolean = false,
        val emailError: Boolean = false,
        val passwordError: Boolean = false
    ) : UiState {

        fun isValid(): Boolean {
            return login.isNotEmpty() &&
                    firstName.isNotEmpty() &&
                    lastName.isNotEmpty() &&
                    email.isNotEmpty() &&
                    password.isNotEmpty() &&
                    passwordRepeat.isNotEmpty()
        }
    }

    sealed interface Event : UiEvent {
        data class OnLoginChanged(val inputLogin: String) : Event
        data class OnFirstNameChanged(val inputFirstName: String) : Event
        data class OnLastNameChanged(val inputLastName: String) : Event
        data class OnEmailChanged(val inputEmail: String) : Event
        data class OnPasswordChanged(val inputPassword: String) : Event
        data class OnPasswordRepeatChanged(val inputPasswordRepeat: String) : Event
        object OnRegisterClicked : Event
    }

    sealed interface Effect : UiEffect {
        data class ShowError(val errorMessage: String) : Effect
        object OpenMainScreen : Effect
    }

}