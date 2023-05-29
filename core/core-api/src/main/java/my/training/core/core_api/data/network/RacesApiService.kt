package my.training.core.core_api.data.network

import my.training.core.core_api.data.model.request.race.RaceRequestBody
import my.training.core.core_api.data.model.response.race.RaceResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RacesApiService {

    @POST("/race")
    suspend fun addRace(
        @Body requestBody: RaceRequestBody
    ): Response<RaceResponseBody>

    @GET("/races")
    suspend fun getRaces(): Response<List<RaceResponseBody>>
}