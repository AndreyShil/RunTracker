package my.training.feature.auth.domain

import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.model.user.AuthUserModel
import my.training.core.core_api.data.model.user.register.RegisterData
import my.training.core.core_api.domain.repository.UserRepository
import javax.inject.Inject

internal class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(registerData: RegisterData): NetworkResponse<AuthUserModel> {
        return userRepository.register(registerData)
    }

}