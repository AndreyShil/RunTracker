package my.training.feature.auth.presentation.sign_in

import androidx.lifecycle.viewModelScope
import androidx.test.espresso.idling.CountingIdlingResource
import kotlinx.coroutines.launch
import my.training.core.core_api.domain.model.user.login.LoginData
import my.training.core.core_api.domain.preferences.Preferences
import my.training.core.core_api.extensions.doOnFailure
import my.training.core.core_api.extensions.doOnSuccess
import my.training.core.core_api.extensions.getErrorMessage
import my.training.core.ui.base.BaseViewModel
import my.training.feature.auth.data.AuthRepository
import java.util.Optional

internal class SignInViewModel(
    private val authRepository: AuthRepository,
    private val preferences: Preferences,
    private val idlingResource: Optional<CountingIdlingResource>
) : BaseViewModel<SignInContract.Event, SignInContract.State, SignInContract.Effect>() {

    override fun createInitialState(): SignInContract.State {
        return SignInContract.State()
    }

    override fun handleEvent(event: SignInContract.Event) {
        when (event) {
            is SignInContract.Event.OnLoginChanged -> {
                setState {
                    copy(login = event.inputLogin)
                }
            }

            is SignInContract.Event.OnPasswordChanged -> {
                setState {
                    copy(password = event.inputPassword)
                }
            }

            SignInContract.Event.OnLoginClicked -> {
                doLoginRequest()
            }
        }
    }

    private fun doLoginRequest() {
        setLoadingState(true)
        idlingResource.ifPresent { it.increment() }
        viewModelScope.launch {
            authRepository.login(uiState.value.toLoginData())
                .doOnSuccess {
                    preferences.saveAccessToken(it.accessToken)
                    setEffect {
                        SignInContract.Effect.OpenMainScreen
                    }
                    setLoadingState(false)
                    idlingResource.ifPresent { it.decrement() }
                }
                .doOnFailure { failure ->
                    setEffect {
                        SignInContract.Effect.ShowError(failure.getErrorMessage())
                    }
                    setLoadingState(false)
                    idlingResource.ifPresent { it.decrement() }
                }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        setState {
            copy(isLoading = isLoading)
        }
    }

    private fun SignInContract.State.toLoginData(): LoginData {
        return LoginData(
            login = login,
            password = password
        )
    }
}
