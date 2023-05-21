package my.training.feature.profile.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.feature.profile.domain.GetProfileFlowUseCase
import javax.inject.Inject

internal class ProfileViewModelFactory @Inject constructor(
    private val getProfileFlowUseCase: GetProfileFlowUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(
            getProfile = getProfileFlowUseCase
        ) as T
    }
}