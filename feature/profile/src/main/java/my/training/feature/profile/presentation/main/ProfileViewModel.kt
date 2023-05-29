package my.training.feature.profile.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import my.training.core.core_api.domain.model.user.User
import my.training.core.core_api.domain.repository.UserRepository

internal class ProfileViewModel(
    userRepository: UserRepository
) : ViewModel() {

    val profileFlow = userRepository.getLocalProfileFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            User()
        )

}