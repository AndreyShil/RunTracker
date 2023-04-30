package my.training.feature.auth.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.core.core_api.domain.repository.UserRepository
import javax.inject.Inject

internal class SignUpViewModelFactory @Inject constructor(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(userRepository) as T
    }
}