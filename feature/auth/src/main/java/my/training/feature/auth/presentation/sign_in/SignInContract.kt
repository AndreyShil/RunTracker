package my.training.feature.auth.presentation.sign_in

import my.training.core.ui.base.UiEffect
import my.training.core.ui.base.UiEvent
import my.training.core.ui.base.UiState

internal object SignInContract {

    data class State(
        val login: String = "",
        val password: String = "",
        val isLoading: Boolean = false
    ) : UiState {

        fun isValid(): Boolean {
            return login.isNotEmpty() && password.isNotEmpty()
        }
    }

    sealed interface Event : UiEvent {
        data class OnLoginChanged(val inputLogin: String) : Event
        data class OnPasswordChanged(val inputPassword: String) : Event
        object OnLoginClicked : Event
    }

    sealed interface Effect : UiEffect {
        data class ShowError(val errorMessage: String) : Effect
        object OpenMainScreen : Effect
    }

}