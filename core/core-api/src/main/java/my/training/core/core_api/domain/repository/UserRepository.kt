package my.training.core.core_api.domain.repository

import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.model.user.AuthUserModel
import my.training.core.core_api.data.model.user.UserLogin
import my.training.core.core_api.data.model.user.User
import my.training.core.core_api.data.model.user.UserRegister

interface UserRepository {

    suspend fun login(model: UserLogin): NetworkResponse<AuthUserModel>

    suspend fun register(model: UserRegister): NetworkResponse<AuthUserModel>

    suspend fun loadProfile(): NetworkResponse<User>
}