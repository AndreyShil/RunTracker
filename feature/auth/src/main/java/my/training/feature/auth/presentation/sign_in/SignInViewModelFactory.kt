package my.training.feature.auth.presentation.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.feature.auth.domain.InsertUserToDatabaseUseCase
import my.training.feature.auth.domain.LoginUserUseCase
import my.training.feature.auth.domain.SaveAccessTokenUseCase
import javax.inject.Inject

internal class SignInViewModelFactory @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val saveAccessTokenUseCase: SaveAccessTokenUseCase,
    private val insertUserToDatabaseUseCase: InsertUserToDatabaseUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignInViewModel(
            loginUser = loginUserUseCase,
            saveAccessToken = saveAccessTokenUseCase,
            insertUserToDatabase = insertUserToDatabaseUseCase
        ) as T
    }
}