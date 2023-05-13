package my.training.core.core_impl.data.repository

import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.model.race.Race
import my.training.core.core_api.domain.repository.RaceRepository
import my.training.core.core_impl.data.model.response.RaceResponseBody
import my.training.core.core_impl.mapper.toModel
import javax.inject.Inject

internal class RaceRepositoryImpl @Inject constructor() : RaceRepository {

    override suspend fun getRaces(): NetworkResponse<List<Race>> {
        return NetworkResponse.Success(mockRaces().map { it.toModel() })
    }

    private fun mockRaces(): List<RaceResponseBody> {
        return listOf(
            RaceResponseBody(
                id = "0",
                date = null,
                distance = 100,
                duration = 20_000,
                mapScreenshot = null
            ),
            RaceResponseBody(
                id = "1",
                date = null,
                distance = 1500,
                duration = 180_000,
                mapScreenshot = null
            ),
            RaceResponseBody(
                id = "2",
                date = null,
                distance = 3500,
                duration = 540_000,
                mapScreenshot = null
            )
        )
    }
}