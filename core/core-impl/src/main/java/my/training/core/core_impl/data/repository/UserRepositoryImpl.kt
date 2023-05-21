package my.training.core.core_impl.data.repository

import android.content.Context
import android.os.Build
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.model.user.AuthUserModel
import my.training.core.core_api.data.model.user.DeviceInfo
import my.training.core.core_api.data.model.user.User
import my.training.core.core_api.data.model.user.login.LoginData
import my.training.core.core_api.data.model.user.login.UserLogin
import my.training.core.core_api.data.model.user.register.RegisterData
import my.training.core.core_api.data.model.user.register.UserRegister
import my.training.core.core_api.di.qualifiers.AppContext
import my.training.core.core_api.di.qualifiers.DispatcherIO
import my.training.core.core_api.domain.repository.UserRepository
import my.training.core.core_api.extensions.getDeviceId
import my.training.core.core_impl.data.database.dao.UserDao
import my.training.core.core_impl.data.network.services.UserApiService
import my.training.core.core_impl.mapper.toBody
import my.training.core.core_impl.mapper.toDto
import my.training.core.core_impl.mapper.toModel
import my.training.core.core_impl.utils.safeNetworkCall
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    @AppContext private val appContext: Context,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val userApiService: UserApiService,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun login(
        data: LoginData
    ): NetworkResponse<AuthUserModel> {
        val response = safeNetworkCall(
            dispatcher = dispatcherIO,
            call = {
                userApiService.login(
                    UserLogin(
                        data = data,
                        deviceInfo = getDeviceInfo()
                    ).toBody()
                )
            },
            converter = { it?.toModel() ?: AuthUserModel() }
        )
        if (response is NetworkResponse.Success) {
            updateLocalUser(response.data.user)
        }

        return response
    }

    override suspend fun register(
        data: RegisterData
    ): NetworkResponse<AuthUserModel> {
        val response = safeNetworkCall(
            dispatcher = dispatcherIO,
            call = {
                userApiService.register(
                    UserRegister(
                        data = data,
                        deviceInfo = getDeviceInfo()
                    ).toBody()
                )
            },
            converter = { it?.toModel() ?: AuthUserModel() }
        )
        if (response is NetworkResponse.Success) {
            updateLocalUser(response.data.user)
        }

        return response
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

    private suspend fun updateLocalUser(user: User) {
        userDao.insert(user.toDto())
    }

    private fun getDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            deviceId = appContext.getDeviceId(),
            deviceModel = getDeviceModel()
        )
    }

    private fun getDeviceModel(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL}"
    }

}