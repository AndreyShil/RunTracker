package my.training.feature.auth.presentation.sign_up

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.training.core.core_api.data.model.user.register.RegisterData
import my.training.core.core_api.extensions.doOnFailure
import my.training.core.core_api.extensions.doOnSuccess
import my.training.core.core_api.extensions.getErrorMessage
import my.training.core.ui.base.BaseViewModel
import my.training.core.ui.extensions.isValidEmail
import my.training.feature.auth.domain.RegisterUserUseCase
import my.training.feature.auth.domain.SaveAccessTokenUseCase

internal class SignUpViewModel(
    private val registerUser: RegisterUserUseCase,
    private val saveAccessToken: SaveAccessTokenUseCase
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
        viewModelScope.launch {
            registerUser(uiState.value.toRegisterData())
                .doOnSuccess {
                    saveAccessToken(it.accessToken)
                    setEffect {
                        SignUpContract.Effect.OpenMainScreen
                    }
                    setLoadingState(false)
                }
                .doOnFailure { failure ->
                    setEffect {
                        SignUpContract.Effect.ShowError(failure.getErrorMessage())
                    }
                    setLoadingState(false)
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