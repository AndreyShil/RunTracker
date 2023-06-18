package my.training.core.core_api.data.network

import my.training.core.core_api.data.model.response.user.SessionResponseBody
import my.training.core.core_api.data.model.response.user.UserResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {

    @GET("/profile")
    suspend fun getProfile(): Response<UserResponseBody>

    @DELETE("/logout")
    suspend fun logout(): Response<Unit>

    @GET("/sessions")
    suspend fun getActiveSessions(): Response<List<SessionResponseBody>>

    @DELETE("/sessions")
    suspend fun removeSession(
        @Query("id") sessionId: String
    ): Response<List<SessionResponseBody>>
}