package my.training.core.core_api.domain.repository

import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.model.race.Race

interface RaceRepository {

    suspend fun getRaces(): NetworkResponse<List<Race>>

}