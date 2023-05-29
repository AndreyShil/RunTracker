package my.training.core.core_api.domain.model.user

data class DeviceInfo(
    val deviceId: String,
    val deviceModel: String,
    val deviceType: String = "Android"
)