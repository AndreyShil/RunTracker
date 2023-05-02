package my.training.feature.auth.domain

import my.training.core.core_api.data.model.user.User
import my.training.core.core_api.domain.repository.UserRepository
import javax.inject.Inject

internal class InsertUserToDatabaseUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(user: User) {
        userRepository.updateLocalUser(user)
    }

}