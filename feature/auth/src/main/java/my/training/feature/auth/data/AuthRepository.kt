package my.training.feature.auth.data

import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.domain.model.user.AuthUserModel
import my.training.core.core_api.domain.model.user.login.LoginData
import my.training.core.core_api.domain.model.user.register.RegisterData

internal interface AuthRepository {

    suspend fun login(data: LoginData): NetworkResponse<AuthUserModel>
    suspend fun register(data: RegisterData): NetworkResponse<AuthUserModel>
}