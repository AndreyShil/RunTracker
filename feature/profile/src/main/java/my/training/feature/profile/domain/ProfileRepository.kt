package my.training.feature.profile.domain

import my.training.core.core_api.data.model.NetworkResponse
import my.training.feature.profile.domain.model.Session

internal interface ProfileRepository {

    suspend fun loadActiveSessions(): NetworkResponse<List<Session>>

    suspend fun removeSession(sessionId: String): NetworkResponse<List<Session>>
}