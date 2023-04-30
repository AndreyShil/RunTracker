package my.training.core.core_api.data.model.user

data class DeviceInfo(
    val deviceId: String,
    val deviceModel: String,
    val deviceType: String = "Android"
)