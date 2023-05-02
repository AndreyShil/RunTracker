package my.training.runtracker.presentation.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.runtracker.domain.InsertUserToDatabaseUseCase
import my.training.runtracker.domain.LoadProfileUseCase
import javax.inject.Inject

class MainActivityViewModelFactory @Inject constructor(
    private val loadProfileUseCase: LoadProfileUseCase,
    private val insertUserToDatabaseUseCase: InsertUserToDatabaseUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(
            loadProfile = loadProfileUseCase,
            insertUserToDatabase = insertUserToDatabaseUseCase
        ) as T
    }
}