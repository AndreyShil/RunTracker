package my.training.feature.auth.presentation.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.core.core_api.domain.preferences.Preferences
import my.training.feature.auth.domain.AuthRepository
import javax.inject.Inject

internal class SignInViewModelFactory @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferences: Preferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignInViewModel(
            authRepository = authRepository,
            preferences = preferences
        ) as T
    }
}