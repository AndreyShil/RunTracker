package my.training.feature.profile.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.feature.profile.domain.GetProfileUseCase
import javax.inject.Inject

internal class ProfileViewModelFactory @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(
            getProfile = getProfileUseCase
        ) as T
    }
}