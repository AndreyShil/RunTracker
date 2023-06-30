package my.training.feature.stats.data

import kotlinx.coroutines.CoroutineDispatcher
import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.network.RacesApiService
import my.training.core.core_api.di.qualifiers.DispatcherIO
import my.training.core.core_api.safeNetworkCall
import my.training.feature.stats.data.model.Stats
import javax.inject.Inject

internal class StatsRepositoryImpl @Inject constructor(
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher,
    private val racesApiService: RacesApiService
) : StatsRepository {

    override suspend fun loadStats(dayCount: Int): NetworkResponse<List<Stats>> {
        return safeNetworkCall(
            dispatcher = dispatcherIO,
            call = { racesApiService.getStats(dayCount) },
            converter = { responseList ->
                responseList?.map { it.toModel() }.orEmpty()
            }
        )
    }
}