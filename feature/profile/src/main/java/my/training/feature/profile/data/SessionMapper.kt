package my.training.feature.profile.data

import my.training.core.core_api.data.model.response.user.SessionResponseBody
import my.training.feature.profile.data.model.Session

internal fun SessionResponseBody.toModel(): Session {
    return Session(
        id = id ?: "",
        deviceModel = deviceModel ?: "",
        deviceType = deviceType ?: "",
        isCurrentSession = isCurrentSession ?: false
    )
}