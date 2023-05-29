package my.training.feature.profile.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.core.core_api.domain.repository.UserRepository
import javax.inject.Inject

internal class ProfileViewModelFactory @Inject constructor(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(
            userRepository = userRepository
        ) as T
    }
}