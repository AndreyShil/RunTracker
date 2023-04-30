package my.training.core.core_impl.data.network

import my.training.core.core_impl.data.model.request.UserLoginRequestBody
import my.training.core.core_impl.data.model.request.UserRegisterRequestBody
import my.training.core.core_impl.data.model.response.AuthUserResponseBody
import my.training.core.core_impl.data.model.response.UserResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

internal interface UserApiService {

    @POST("/login")
    suspend fun login(
        @Body requestBody: UserLoginRequestBody
    ): Response<AuthUserResponseBody>

    @POST("/register")
    suspend fun register(
        @Body requestBody: UserRegisterRequestBody
    ): Response<AuthUserResponseBody>

    @GET("/profile")
    suspend fun getProfile(): Response<UserResponseBody>

}