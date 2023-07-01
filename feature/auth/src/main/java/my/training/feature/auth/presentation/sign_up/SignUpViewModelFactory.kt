package my.training.feature.auth.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.idling.CountingIdlingResource
import my.training.core.core_api.domain.preferences.Preferences
import my.training.feature.auth.data.AuthRepository
import java.util.Optional
import javax.inject.Inject

internal class SignUpViewModelFactory @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferences: Preferences,
    private val idlingResource: Optional<CountingIdlingResource>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(
            authRepository = authRepository,
            preferences = preferences,
            idlingResource = idlingResource
        ) as T
    }
}