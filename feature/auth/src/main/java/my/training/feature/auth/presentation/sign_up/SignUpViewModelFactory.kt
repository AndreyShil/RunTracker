package my.training.feature.auth.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.feature.auth.domain.InsertUserToDatabaseUseCase
import my.training.feature.auth.domain.RegisterUserUseCase
import my.training.feature.auth.domain.SaveAccessTokenUseCase
import javax.inject.Inject

internal class SignUpViewModelFactory @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val saveAccessTokenUseCase: SaveAccessTokenUseCase,
    private val insertUserToDatabaseUseCase: InsertUserToDatabaseUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(
            registerUser = registerUserUseCase,
            saveAccessToken = saveAccessTokenUseCase,
            insertUserToDatabase = insertUserToDatabaseUseCase
        ) as T
    }
}