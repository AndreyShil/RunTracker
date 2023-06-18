package my.training.runtracker.presentation.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import my.training.core.core_api.domain.preferences.AppSettings
import my.training.core.core_api.domain.preferences.Preferences
import my.training.core.core_api.domain.repository.UserRepository
import my.training.core.core_api.extensions.doOnFailure
import my.training.core.core_api.extensions.doOnSuccess
import my.training.core.core_api.extensions.getErrorMessage

internal class MainActivityViewModel(
    private val userRepository: UserRepository,
    private val preferences: Preferences,
    appSettings: AppSettings
) : ViewModel() {

    private val _effect: Channel<MainActivityEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        appSettings.installTheme()
        viewModelScope.launch {
            if (preferences.getAccessToken().isNullOrEmpty()) {
                _effect.send(MainActivityEffect.OpenAuthGraph)
            } else {
                downloadProfile()
            }
        }
    }

    private fun downloadProfile() {
        viewModelScope.launch {
            userRepository.loadProfile()
                .doOnSuccess {
                    _effect.send(MainActivityEffect.OpenMainGraph)
                }.doOnFailure {
                    _effect.send(MainActivityEffect.ShowError(it.getErrorMessage()))
                    _effect.send(MainActivityEffect.OpenAuthGraph)
                }
        }
    }
}
