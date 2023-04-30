package my.training.core.core_impl.repository

import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.model.user.AuthUserModel
import my.training.core.core_api.data.model.user.User
import my.training.core.core_api.data.model.user.UserLogin
import my.training.core.core_api.data.model.user.UserRegister
import my.training.core.core_api.domain.repository.UserRepository
import my.training.core.core_impl.data.network.UserApiService
import my.training.core.core_impl.mapper.toBody
import my.training.core.core_impl.mapper.toModel
import my.training.core.core_impl.utils.safeNetworkCall
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRepository {

    override suspend fun login(
        model: UserLogin
    ): NetworkResponse<AuthUserModel> {
        return safeNetworkCall(
            call = {
                userApiService.login(model.toBody())
            }
        ) {
            it?.toModel() ?: AuthUserModel()
        }
    }

    override suspend fun register(
        model: UserRegister
    ): NetworkResponse<AuthUserModel> {
        return safeNetworkCall(
            call = {
                userApiService.register(model.toBody())
            }
        ) {
            it?.toModel() ?: AuthUserModel()
        }
    }

    override suspend fun loadProfile(): NetworkResponse<User> {
        return safeNetworkCall(
            call = userApiService::getProfile,
        ) {
            it?.toModel() ?: User()
        }
    }

}