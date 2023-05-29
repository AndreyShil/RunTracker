package my.training.feature.tracker.domain

import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.domain.model.race.Race
import my.training.core.core_api.domain.model.race.RaceCreating

internal interface RaceRepository {

    suspend fun createRace(race: RaceCreating): NetworkResponse<Race>

    suspend fun getRaces(): NetworkResponse<List<Race>>
}