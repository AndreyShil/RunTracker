package my.training.runtracker.domain

import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.model.user.User
import my.training.core.core_api.domain.repository.UserRepository
import javax.inject.Inject

class LoadProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): NetworkResponse<User> {
        return userRepository.loadProfile()
    }
}