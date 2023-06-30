package my.training.feature.profile.data

import kotlinx.coroutines.CoroutineDispatcher
import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.network.UserApiService
import my.training.core.core_api.di.qualifiers.DispatcherIO
import my.training.core.core_api.safeNetworkCall
import my.training.feature.profile.data.model.Session
import javax.inject.Inject

internal class ProfileRepositoryImpl @Inject constructor(
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val userApiService: UserApiService
) : ProfileRepository {

    override suspend fun loadActiveSessions(): NetworkResponse<List<Session>> {
        return safeNetworkCall(
            dispatcher = dispatcherIO,
            call = { userApiService.getActiveSessions() },
            converter = { responseList ->
                responseList?.map { it.toModel() }.orEmpty()
            }
        )
    }

    override suspend fun removeSession(
        sessionId: String
    ): NetworkResponse<List<Session>> {
        return safeNetworkCall(
            dispatcher = dispatcherIO,
            call = { userApiService.removeSession(sessionId) },
            converter = { responseList ->
                responseList?.map { it.toModel() }.orEmpty()
            }
        )
    }
}