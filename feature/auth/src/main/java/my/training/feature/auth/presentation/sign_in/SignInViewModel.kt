package my.training.feature.auth.presentation.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import my.training.core.core_api.data.model.user.UserLogin
import my.training.core.core_api.domain.repository.UserRepository
import my.training.core.core_api.extensions.doOnFailure
import my.training.core.core_api.extensions.doOnSuccess

internal class SignInViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState

    fun doAction(action: SignInAction) {
        when (action) {
            is SignInAction.OnLoginChanged -> _uiState.update {
                it.copy(login = action.inputLogin)
            }

            is SignInAction.OnPasswordChanged -> _uiState.update {
                it.copy(password = action.inputPassword)
            }

            SignInAction.DoLoginRequest -> {
                doLoginRequest()
            }
        }
    }

    private fun doLoginRequest() {
        viewModelScope.launch {
            userRepository.login(uiState.value.toLoginModel())
                .doOnSuccess {

                }
                .doOnFailure { failure ->
                    _uiState.update {
                        it.copy(error = failure.error)
                    }
//                    failure.error?.printStackTrace()
                }
        }
    }

    private fun SignInUiState.toLoginModel(): UserLogin {
        return UserLogin(
            login = login,
            password = password
        )
    }

}