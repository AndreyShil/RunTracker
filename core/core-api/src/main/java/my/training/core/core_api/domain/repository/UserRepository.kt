package my.training.core.core_api.domain.repository

import kotlinx.coroutines.flow.Flow
import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.model.user.AuthUserModel
import my.training.core.core_api.data.model.user.User
import my.training.core.core_api.data.model.user.login.LoginData
import my.training.core.core_api.data.model.user.register.RegisterData

interface UserRepository {

    suspend fun login(data: LoginData): NetworkResponse<AuthUserModel>

    suspend fun register(data: RegisterData): NetworkResponse<AuthUserModel>

    suspend fun loadProfile(): NetworkResponse<User>

    fun getLocalProfileFlow(): Flow<User?>
}