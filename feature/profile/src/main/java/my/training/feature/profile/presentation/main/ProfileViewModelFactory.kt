package my.training.feature.profile.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.core.core_api.domain.preferences.AppSettings
import my.training.core.core_api.domain.repository.UserRepository
import my.training.feature.profile.data.ProfileRepository
import javax.inject.Inject

internal class ProfileViewModelFactory @Inject constructor(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
    private val appSettings: AppSettings
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(
            userRepository = userRepository,
            profileRepository = profileRepository,
            appSettings = appSettings
        ) as T
    }
}