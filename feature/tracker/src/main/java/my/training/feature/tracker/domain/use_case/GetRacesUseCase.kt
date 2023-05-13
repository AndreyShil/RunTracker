package my.training.feature.tracker.domain.use_case

import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.model.race.Race
import my.training.core.core_api.domain.repository.RaceRepository
import javax.inject.Inject

internal class GetRacesUseCase @Inject constructor(
    private val racesRepository: RaceRepository
) {

    suspend operator fun invoke(): NetworkResponse<List<Race>> {
        return racesRepository.getRaces()
    }
}