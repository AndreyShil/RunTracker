package my.training.core.core_api.mapper

import my.training.core.core_api.data.model.request.DeviceInfoRequestBody
import my.training.core.core_api.data.model.request.user.LoginDataRequestBody
import my.training.core.core_api.data.model.request.user.RegisterDataRequestBody
import my.training.core.core_api.data.model.request.user.UserLoginRequestBody
import my.training.core.core_api.data.model.request.user.UserRegisterRequestBody
import my.training.core.core_api.data.model.response.user.AuthUserResponseBody
import my.training.core.core_api.data.model.response.user.UserResponseBody
import my.training.core.core_api.domain.model.user.AuthUserModel
import my.training.core.core_api.domain.model.user.DeviceInfo
import my.training.core.core_api.domain.model.user.User
import my.training.core.core_api.domain.model.user.login.LoginData
import my.training.core.core_api.domain.model.user.login.UserLogin
import my.training.core.core_api.domain.model.user.register.RegisterData
import my.training.core.core_api.domain.model.user.register.UserRegister

fun UserLogin.toBody(): UserLoginRequestBody {
    return UserLoginRequestBody(
        loginData = data.toBody(),
        deviceInfo = deviceInfo.toBody()
    )
}

fun LoginData.toBody(): LoginDataRequestBody {
    return LoginDataRequestBody(
        login = login,
        password = password
    )
}

fun UserRegister.toBody(): UserRegisterRequestBody {
    return UserRegisterRequestBody(
        registerData = data.toBody(),
        deviceInfo = deviceInfo.toBody()
    )
}

fun RegisterData.toBody(): RegisterDataRequestBody {
    return RegisterDataRequestBody(
        login = login,
        password = password,
        firstName = firstName,
        lastName = lastName,
        email = email
    )
}

fun AuthUserResponseBody.toModel(): AuthUserModel {
    return AuthUserModel(
        accessToken = token ?: "",
        user = user?.toModel() ?: User()
    )
}

fun UserResponseBody.toModel(): User {
    return User(
        login = login,
        firstName = firstName,
        lastName = lastName,
        email = email,
        photo = photo
    )
}

fun DeviceInfo.toBody(): DeviceInfoRequestBody {
    return DeviceInfoRequestBody(
        deviceId = deviceId,
        deviceModel = deviceModel,
        deviceType = deviceType
    )
}