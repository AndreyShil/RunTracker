package my.training.feature.profile.domain

import kotlinx.coroutines.flow.Flow
import my.training.core.core_api.data.model.user.User
import my.training.core.core_api.domain.repository.UserRepository
import javax.inject.Inject

internal class GetProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(): Flow<User?> {
        return userRepository.getLocalProfile()
    }
}