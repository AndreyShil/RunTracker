package my.training.feature.profile.presentation.main

import my.training.core.core_api.domain.model.enums.AppTheme
import my.training.core.core_api.domain.model.user.User
import my.training.core.ui.base.UiEffect
import my.training.core.ui.base.UiEvent
import my.training.core.ui.base.UiState
import my.training.feature.profile.domain.model.Session

internal object ProfileContract {

    data class State(
        val user: User = User(),
        val sessions: List<Session> = emptyList(),
        val isLoading: Boolean = false
    ) : UiState

    sealed interface Event : UiEvent {
        data class OnUserReceived(val user: User?) : Event
        data class RemoveSession(val sessionId: String) : Event
        data class OnAppThemeChanged(val appTheme: AppTheme) : Event
        object OnLogoutClicked : Event
    }

    sealed interface Effect : UiEffect {
        data class ShowError(val errorMessage: String) : Effect
        data class SetInitialThemeSetting(val appTheme: AppTheme) : Effect
        object OpenAuthScreen : Effect
    }
}