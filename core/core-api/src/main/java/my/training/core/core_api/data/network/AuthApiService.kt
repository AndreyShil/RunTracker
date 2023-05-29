package my.training.core.core_api.data.network

import my.training.core.core_api.data.model.request.user.UserLoginRequestBody
import my.training.core.core_api.data.model.request.user.UserRegisterRequestBody
import my.training.core.core_api.data.model.response.user.AuthUserResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("/login")
    suspend fun login(
        @Body requestBody: UserLoginRequestBody
    ): Response<AuthUserResponseBody>

    @POST("/register")
    suspend fun register(
        @Body requestBody: UserRegisterRequestBody
    ): Response<AuthUserResponseBody>

}