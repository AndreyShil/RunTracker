package my.training.core.core_impl.mapper

import my.training.core.core_api.domain.model.user.User
import my.training.core.core_impl.data.model.dto.UserDTO

internal fun User.toDto(): UserDTO {
    return UserDTO(
        login = login ?: "",
        firstName = firstName,
        lastName = lastName,
        email = email,
        photo = photo
    )
}

internal fun UserDTO.toModel(): User {
    return User(
        login = login,
        firstName = firstName,
        lastName = lastName,
        email = email,
        photo = photo
    )
}