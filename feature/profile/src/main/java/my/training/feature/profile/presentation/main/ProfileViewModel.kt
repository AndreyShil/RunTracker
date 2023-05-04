package my.training.feature.profile.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import my.training.core.core_api.data.model.user.User
import my.training.feature.profile.domain.GetProfileUseCase

internal class ProfileViewModel(
    getProfile: GetProfileUseCase
) : ViewModel() {

    val profileFlow = getProfile()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            User()
        )

}