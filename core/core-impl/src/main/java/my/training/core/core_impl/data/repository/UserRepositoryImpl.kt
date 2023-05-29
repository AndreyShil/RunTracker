package my.training.core.core_impl.data.repository

import android.content.Context
import android.os.Build
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.di.qualifiers.AppContext
import my.training.core.core_api.di.qualifiers.DispatcherIO
import my.training.core.core_api.domain.model.user.DeviceInfo
import my.training.core.core_api.domain.model.user.User
import my.training.core.core_api.domain.repository.UserRepository
import my.training.core.core_api.extensions.getDeviceId
import my.training.core.core_api.safeNetworkCall
import my.training.core.core_impl.data.database.dao.UserDao
import my.training.core.core_impl.data.network.services.UserApiService
import my.training.core.core_api.mapper.toModel
import my.training.core.core_impl.mapper.toDto
import my.training.core.core_impl.mapper.toModel
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    @AppContext private val appContext: Context,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val userApiService: UserApiService,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun updateLocalUser(user: User) {
        userDao.insert(user.toDto())
    }

    override suspend fun loadProfile(): NetworkResponse<User> {
        val response = safeNetworkCall(
            dispatcher = dispatcherIO,
            call = userApiService::getProfile,
            converter = { it?.toModel() ?: User() }
        )

        if (response is NetworkResponse.Success) {
            updateLocalUser(response.data)
        }

        return response
    }

    override fun getLocalProfileFlow(): Flow<User?> {
        return userDao.getUserFlow().map { it.toModel() }
    }

    override fun getDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            deviceId = appContext.getDeviceId(),
            deviceModel = getDeviceModel()
        )
    }

    private fun getDeviceModel(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL}"
    }

}