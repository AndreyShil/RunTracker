package my.training.runtracker.presentation.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.core.core_api.domain.preferences.AppSettings
import my.training.core.core_api.domain.preferences.Preferences
import my.training.core.core_api.domain.repository.UserRepository
import javax.inject.Inject

internal class MainActivityViewModelFactory @Inject constructor(
    private val userRepository: UserRepository,
    private val preferences: Preferences,
    private val appSettings: AppSettings
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(
            userRepository = userRepository,
            preferences = preferences,
            appSettings = appSettings
        ) as T
    }
}