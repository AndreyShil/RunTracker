package my.training.feature.profile.presentation.main

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import my.training.core.core_api.domain.model.user.User
import my.training.core.core_api.domain.preferences.AppSettings
import my.training.core.core_api.domain.repository.UserRepository
import my.training.core.core_api.extensions.doOnFailure
import my.training.core.core_api.extensions.doOnSuccess
import my.training.core.core_api.extensions.getErrorMessage
import my.training.core.ui.base.BaseViewModel
import my.training.feature.profile.domain.ProfileRepository

internal class ProfileViewModel(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
    private val appSettings: AppSettings
) : BaseViewModel<ProfileContract.Event, ProfileContract.State, ProfileContract.Effect>() {

    init {
        getAppTheme()
        loadSessions()
    }

    override fun createInitialState(): ProfileContract.State = ProfileContract.State()

    override fun handleEvent(event: ProfileContract.Event) {
        when (event) {
            is ProfileContract.Event.OnUserReceived -> {
                setState {
                    copy(user = event.user ?: User())
                }
            }

            is ProfileContract.Event.RemoveSession -> {
                removeSession(event.sessionId)
            }

            is ProfileContract.Event.OnAppThemeChanged -> {
                appSettings.setAppTheme(event.appTheme)
            }

            ProfileContract.Event.OnLogoutClicked -> {
                doLogoutRequest()
            }
        }
    }

    val profileFlow = userRepository.getLocalProfileFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            User()
        )

    private fun getAppTheme() {
        setEffect {
            ProfileContract.Effect.SetInitialThemeSetting(
                appTheme = appSettings.getAppTheme()
            )
        }
    }

    private fun loadSessions() {
        viewModelScope.launch {
            profileRepository.loadActiveSessions()
                .doOnSuccess { activeSessions ->
                    setState {
                        copy(sessions = activeSessions)
                    }
                }
                .doOnFailure {
                    setEffect {
                        ProfileContract.Effect.ShowError(
                            errorMessage = it.getErrorMessage()
                        )
                    }
                }
        }
    }

    private fun removeSession(sessionId: String) {
        viewModelScope.launch {
            profileRepository.removeSession(sessionId)
                .doOnSuccess { activeSessions ->
                    setState {
                        copy(sessions = activeSessions)
                    }
                }
                .doOnFailure {
                    setEffect {
                        ProfileContract.Effect.ShowError(
                            errorMessage = it.getErrorMessage()
                        )
                    }
                }
        }
    }

    private fun doLogoutRequest() {
        viewModelScope.launch {
            userRepository.logout()
                .doOnSuccess {
                    setEffect {
                        ProfileContract.Effect.OpenAuthScreen
                    }
                }
                .doOnFailure {
                    setEffect {
                        ProfileContract.Effect.ShowError(
                            errorMessage = it.getErrorMessage()
                        )
                    }
                }
        }
    }
}