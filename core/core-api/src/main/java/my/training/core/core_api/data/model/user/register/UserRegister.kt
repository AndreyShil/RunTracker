package my.training.core.core_api.data.model.user.register

import my.training.core.core_api.data.model.user.DeviceInfo

data class UserRegister(
    val data: RegisterData,
    val deviceInfo: DeviceInfo
)