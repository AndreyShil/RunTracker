package my.training.runtracker.presentation.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.runtracker.domain.GetAccessTokenUseCase
import my.training.runtracker.domain.LoadProfileUseCase
import javax.inject.Inject

internal class MainActivityViewModelFactory @Inject constructor(
    private val loadProfileUseCase: LoadProfileUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(
            loadProfile = loadProfileUseCase,
            getAccessToken = getAccessTokenUseCase
        ) as T
    }
}