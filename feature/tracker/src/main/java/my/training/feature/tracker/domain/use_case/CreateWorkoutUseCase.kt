package my.training.feature.tracker.domain.use_case

import my.training.core.core_api.data.model.NetworkResponse
import my.training.core.core_api.data.model.race.Race
import my.training.core.core_api.data.model.race.RaceCreating
import my.training.core.core_api.domain.repository.RaceRepository
import javax.inject.Inject

internal class CreateWorkoutUseCase @Inject constructor(
    private val racesRepository: RaceRepository
) {

    suspend operator fun invoke(race: RaceCreating): NetworkResponse<Race> {
        return racesRepository.createRace(race)
    }
}