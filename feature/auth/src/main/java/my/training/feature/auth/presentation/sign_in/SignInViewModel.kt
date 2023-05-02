package my.training.feature.auth.presentation.sign_in

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.training.core.core_api.data.model.user.login.LoginData
import my.training.core.core_api.extensions.doOnFailure
import my.training.core.core_api.extensions.doOnSuccess
import my.training.core.core_api.extensions.getErrorMessage
import my.training.core.ui.base.BaseViewModel
import my.training.feature.auth.domain.InsertUserToDatabaseUseCase
import my.training.feature.auth.domain.LoginUserUseCase
import my.training.feature.auth.domain.SaveAccessTokenUseCase

internal class SignInViewModel(
    private val loginUser: LoginUserUseCase,
    private val saveAccessToken: SaveAccessTokenUseCase,
    private val insertUserToDatabase: InsertUserToDatabaseUseCase
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
        viewModelScope.launch {
            loginUser(uiState.value.toLoginData())
                .doOnSuccess {
                    saveAccessToken(it.accessToken)
                    insertUserToDatabase(it.user)
                    setEffect {
                        SignInContract.Effect.OpenMainScreen
                    }
                    setLoadingState(false)
                }
                .doOnFailure { failure ->
                    setEffect {
                        SignInContract.Effect.ShowError(failure.getErrorMessage())
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

    private fun SignInContract.State.toLoginData(): LoginData {
        return LoginData(
            login = login,
            password = password
        )
    }

}