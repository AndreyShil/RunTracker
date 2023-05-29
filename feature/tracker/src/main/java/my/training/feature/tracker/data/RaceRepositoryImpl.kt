package my.training.feature.tracker.data

import kotlinx.coroutines.CoroutineDispatcher
import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.domain.model.race.Race
import my.training.core.core_api.domain.model.race.RaceCreating
import my.training.core.core_api.data.network.RacesApiService
import my.training.core.core_api.di.qualifiers.DispatcherIO
import my.training.core.core_api.safeNetworkCall
import my.training.core.core_api.toModel
import my.training.core.core_api.toRequestBody
import my.training.feature.tracker.domain.RaceRepository
import javax.inject.Inject

internal class RaceRepositoryImpl @Inject constructor(
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val racesApiService: RacesApiService
) : RaceRepository {

    override suspend fun createRace(race: RaceCreating): NetworkResponse<Race> {
        return safeNetworkCall(
            dispatcher = dispatcherIO,
            call = { racesApiService.addRace(race.toRequestBody()) },
            converter = { it?.toModel() ?: Race() }
        )
    }

    override suspend fun getRaces(): NetworkResponse<List<Race>> {
        return safeNetworkCall(
            dispatcher = dispatcherIO,
            call = { racesApiService.getRaces() },
            converter = { responseList ->
                responseList?.map { it.toModel() } ?: emptyList()
            }
        )
    }
}