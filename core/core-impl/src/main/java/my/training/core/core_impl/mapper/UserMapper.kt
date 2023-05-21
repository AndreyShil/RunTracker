package my.training.core.core_impl.mapper

import my.training.core.core_api.data.model.user.AuthUserModel
import my.training.core.core_api.data.model.user.DeviceInfo
import my.training.core.core_api.data.model.user.User
import my.training.core.core_api.data.model.user.login.LoginData
import my.training.core.core_api.data.model.user.login.UserLogin
import my.training.core.core_api.data.model.user.register.RegisterData
import my.training.core.core_api.data.model.user.register.UserRegister
import my.training.core.core_impl.data.model.dto.UserDTO
import my.training.core.core_impl.data.model.request.DeviceInfoRequestBody
import my.training.core.core_impl.data.model.request.user.LoginDataRequestBody
import my.training.core.core_impl.data.model.request.user.RegisterDataRequestBody
import my.training.core.core_impl.data.model.request.user.UserLoginRequestBody
import my.training.core.core_impl.data.model.request.user.UserRegisterRequestBody
import my.training.core.core_impl.data.model.response.user.AuthUserResponseBody
import my.training.core.core_impl.data.model.response.user.UserResponseBody

internal fun UserLogin.toBody(): UserLoginRequestBody {
    return UserLoginRequestBody(
        loginData = data.toBody(),
        deviceInfo = deviceInfo.toBody()
    )
}

internal fun LoginData.toBody(): LoginDataRequestBody {
    return LoginDataRequestBody(
        login = login,
        password = password
    )
}

internal fun UserRegister.toBody(): UserRegisterRequestBody {
    return UserRegisterRequestBody(
        registerData = data.toBody(),
        deviceInfo = deviceInfo.toBody()
    )
}

internal fun RegisterData.toBody(): RegisterDataRequestBody {
    return RegisterDataRequestBody(
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

internal fun DeviceInfo.toBody(): DeviceInfoRequestBody {
    return DeviceInfoRequestBody(
        deviceId = deviceId,
        deviceModel = deviceModel,
        deviceType = deviceType
    )
}

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