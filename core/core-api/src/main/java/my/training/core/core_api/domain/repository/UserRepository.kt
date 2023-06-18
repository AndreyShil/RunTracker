package my.training.core.core_api.domain.repository

import kotlinx.coroutines.flow.Flow
import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.domain.model.user.DeviceInfo
import my.training.core.core_api.domain.model.user.User

interface UserRepository {

    suspend fun updateLocalUser(user: User)

    suspend fun loadProfile(): NetworkResponse<User>

    suspend fun logout(): NetworkResponse<Unit>

    fun getLocalProfileFlow(): Flow<User?>

    fun getDeviceInfo(): DeviceInfo
}