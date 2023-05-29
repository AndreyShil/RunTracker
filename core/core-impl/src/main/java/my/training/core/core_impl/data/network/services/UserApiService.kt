package my.training.core.core_impl.data.network.services

import my.training.core.core_api.data.model.response.user.UserResponseBody
import retrofit2.Response
import retrofit2.http.GET

internal interface UserApiService {

    @GET("/profile")
    suspend fun getProfile(): Response<UserResponseBody>

}