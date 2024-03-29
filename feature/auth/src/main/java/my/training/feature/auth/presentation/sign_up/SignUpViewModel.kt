package my.training.feature.auth.presentation.sign_up

import androidx.lifecycle.viewModelScope
import androidx.test.espresso.idling.CountingIdlingResource
import kotlinx.coroutines.launch
import my.training.core.core_api.domain.model.user.register.RegisterData
import my.training.core.core_api.domain.preferences.Preferences
import my.training.core.core_api.extensions.doOnFailure
import my.training.core.core_api.extensions.doOnSuccess
import my.training.core.core_api.extensions.getErrorMessage
import my.training.core.ui.base.BaseViewModel
import my.training.core.ui.extensions.isValidEmail
import my.training.feature.auth.data.AuthRepository
import java.util.Optional

internal class SignUpViewModel(
    private val authRepository: AuthRepository,
    private val preferences: Preferences,
    private val idlingResource: Optional<CountingIdlingResource>
) : BaseViewModel<SignUpContract.Event, SignUpContract.State, SignUpContract.Effect>() {

    override fun createInitialState(): SignUpContract.State {
        return SignUpContract.State()
    }

    override fun handleEvent(event: SignUpContract.Event) {
        when (event) {
            is SignUpContract.Event.OnLoginChanged -> {
                setState { copy(login = event.inputLogin) }
            }

            is SignUpContract.Event.OnFirstNameChanged -> {
                setState { copy(firstName = event.inputFirstName) }
            }

            is SignUpContract.Event.OnLastNameChanged -> {
                setState { copy(lastName = event.inputLastName) }
            }

            is SignUpContract.Event.OnEmailChanged -> {
                setState {
                    copy(
                        email = event.inputEmail,
                        emailError = false
                    )
                }
            }

            is SignUpContract.Event.OnPasswordChanged -> {
                setState {
                    copy(
                        password = event.inputPassword,
                        passwordError = false
                    )
                }
            }

            is SignUpContract.Event.OnPasswordRepeatChanged -> {
                setState {
                    copy(
                        passwordRepeat = event.inputPasswordRepeat,
                        passwordError = false
                    )
                }
            }

            SignUpContract.Event.OnRegisterClicked -> doRegisterRequest()
        }
    }

    private fun doRegisterRequest() {
        if (!currentState.email.isValidEmail()) {
            setState { copy(emailError = true) }
            return
        }
        if (currentState.password != currentState.passwordRepeat) {
            setState { copy(passwordError = true) }
            return
        }
        setLoadingState(true)
        idlingResource.ifPresent { it.increment() }
        viewModelScope.launch {
            authRepository.register(uiState.value.toRegisterData())
                .doOnSuccess { user ->
                    preferences.saveAccessToken(user.accessToken)
                    setEffect {
                        SignUpContract.Effect.OpenMainScreen
                    }
                    setLoadingState(false)
                    idlingResource.ifPresent { it.decrement() }
                }
                .doOnFailure { failure ->
                    setEffect {
                        SignUpContract.Effect.ShowError(failure.getErrorMessage())
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

    private fun SignUpContract.State.toRegisterData(): RegisterData {
        return RegisterData(
            login = login,
            password = password,
            firstName = firstName,
            lastName = lastName,
            email = email
        )
    }
}