package my.training.core.core_impl.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.model.race.Race
import my.training.core.core_api.data.model.race.RaceCreating
import my.training.core.core_api.di.qualifiers.DispatcherIO
import my.training.core.core_api.domain.repository.RaceRepository
import my.training.core.core_impl.data.network.services.RacesApiService
import my.training.core.core_impl.mapper.toModel
import my.training.core.core_impl.mapper.toRequestBody
import my.training.core.core_impl.utils.safeNetworkCall
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

//    private fun mockRaces(): List<RaceResponseBody> {
//        return listOf(
//            RaceResponseBody(
//                id = "0",
//                date = null,
//                distance = 100,
//                duration = 20_000,
//                mapScreen = null
//            ),
//            RaceResponseBody(
//                id = "1",
//                date = null,
//                distance = 1500,
//                duration = 180_000,
//                mapScreen = null
//            ),
//            RaceResponseBody(
//                id = "2",
//                date = null,
//                distance = 3500,
//                duration = 540_000,
//                mapScreen = null
//            )
//        )
//    }
}