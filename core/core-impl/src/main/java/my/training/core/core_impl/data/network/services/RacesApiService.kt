package my.training.core.core_impl.data.network.services

import my.training.core.core_impl.data.model.request.race.RaceRequestBody
import my.training.core.core_impl.data.model.response.race.RaceResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

internal interface RacesApiService {

    @POST("/race")
    suspend fun addRace(
        @Body requestBody: RaceRequestBody
    ): Response<RaceResponseBody>

    @GET("/races")
    suspend fun getRaces(): Response<List<RaceResponseBody>>
}