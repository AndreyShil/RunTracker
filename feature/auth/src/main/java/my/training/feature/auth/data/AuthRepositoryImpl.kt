package my.training.feature.auth.data

import kotlinx.coroutines.CoroutineDispatcher
import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.network.AuthApiService
import my.training.core.core_api.di.qualifiers.DispatcherIO
import my.training.core.core_api.domain.model.user.AuthUserModel
import my.training.core.core_api.domain.model.user.login.LoginData
import my.training.core.core_api.domain.model.user.login.UserLogin
import my.training.core.core_api.domain.model.user.register.RegisterData
import my.training.core.core_api.domain.model.user.register.UserRegister
import my.training.core.core_api.domain.repository.UserRepository
import my.training.core.core_api.mapper.toBody
import my.training.core.core_api.mapper.toModel
import my.training.core.core_api.safeNetworkCall
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val authApiService: AuthApiService
) : AuthRepository {

    override suspend fun login(
        data: LoginData
    ): NetworkResponse<AuthUserModel> {
        val response = safeNetworkCall(
            dispatcher = dispatcherIO,
            call = {
                authApiService.login(
                    UserLogin(
                        data = data,
                        deviceInfo = userRepository.getDeviceInfo()
                    ).toBody()
                )
            },
            converter = { it?.toModel() ?: AuthUserModel() }
        )
        if (response is NetworkResponse.Success) {
            userRepository.updateLocalUser(response.data.user)
        }

        return response
    }

    override suspend fun register(
        data: RegisterData
    ): NetworkResponse<AuthUserModel> {
        val response = safeNetworkCall(
            dispatcher = dispatcherIO,
            call = {
                authApiService.register(
                    UserRegister(
                        data = data,
                        deviceInfo = userRepository.getDeviceInfo()
                    ).toBody()
                )
            },
            converter = { it?.toModel() ?: AuthUserModel() }
        )
        if (response is NetworkResponse.Success) {
            userRepository.updateLocalUser(response.data.user)
        }

        return response
    }

}