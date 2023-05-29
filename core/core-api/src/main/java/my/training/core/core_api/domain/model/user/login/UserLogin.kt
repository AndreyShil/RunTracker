package my.training.core.core_api.domain.model.user.login

import my.training.core.core_api.domain.model.user.DeviceInfo

data class UserLogin(
    val data: LoginData,
    val deviceInfo: DeviceInfo
)