package my.training.core.core_impl.mapper

import my.training.core.core_api.data.model.user.AuthUserModel
import my.training.core.core_api.data.model.user.User
import my.training.core.core_api.data.model.user.UserLogin
import my.training.core.core_api.data.model.user.UserRegister
import my.training.core.core_impl.data.model.request.UserLoginRequestBody
import my.training.core.core_impl.data.model.request.UserRegisterRequestBody
import my.training.core.core_impl.data.model.response.AuthUserResponseBody
import my.training.core.core_impl.data.model.response.UserResponseBody

internal fun UserLogin.toBody(): UserLoginRequestBody {
    return UserLoginRequestBody(
        login = login,
        password = password
    )
}

internal fun UserRegister.toBody(): UserRegisterRequestBody {
    return UserRegisterRequestBody(
        login = login,
        password = password,
        firstName = firstName,
        lastName = lastName,
        email = email
    )
}

internal fun AuthUserResponseBody.toModel(): AuthUserModel {
    return AuthUserModel(
        accessToken = token ?: "",
        user = user?.toModel() ?: User()
    )
}

internal fun UserResponseBody.toModel(): User {
    return User(
        login = login,
        firstName = firstName,
        lastName = lastName,
        email = email,
        photo = photo
    )
}