package my.training.feature.auth.domain

import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.model.user.AuthUserModel
import my.training.core.core_api.data.model.user.login.LoginData
import my.training.core.core_api.domain.repository.UserRepository
import javax.inject.Inject

internal class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(loginData: LoginData): NetworkResponse<AuthUserModel> {
        return userRepository.login(loginData)
    }

}