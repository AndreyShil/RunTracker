package my.training.core.core_api.domain.repository

import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.model.race.Race
import my.training.core.core_api.data.model.race.RaceCreating

interface RaceRepository {

    suspend fun createRace(race: RaceCreating): NetworkResponse<Race>

    suspend fun getRaces(): NetworkResponse<List<Race>>

}