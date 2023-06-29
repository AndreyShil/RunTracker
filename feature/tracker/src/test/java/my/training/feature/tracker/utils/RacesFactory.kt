package my.training.feature.tracker.utils

import my.training.core.core_api.data.model.response.race.RaceResponseBody
import my.training.core.core_api.domain.model.race.Race
import my.training.core.core_api.domain.model.race.RaceCreating
import my.training.core.core_api.toModel

object RacesFactory {
    fun getCreateRaceRequestData(): RaceCreating {
        return RaceCreating(
            date = "2023-05-10T10:12:45.555Z",
            distance = 2560,
            duration = 423_000,
            mapScreen = "mock_map_screen_100"
        )
    }

    fun getCreatedRace(): RaceResponseBody {
        return RaceResponseBody(
            id = "111",
            date = "2023-05-12T10:12:45.555Z",
            distance = 2560,
            duration = 423_000,
            mapScreen = "mock_map_screen_100",
            averageSpeed = 5.0,
            burnedCalories = 210.2
        )
    }

    fun getRaces(): List<RaceResponseBody> {
        return listOf(
            RaceResponseBody(
                id = "100",
                date = "2023-05-10T10:12:45.555Z",
                distance = 2500,
                duration = 450_000,
                mapScreen = "map_screen_100",
                averageSpeed = 6.5,
                burnedCalories = 200.2
            ),
            RaceResponseBody(
                id = "200",
                date = "2023-05-12T10:12:45.555Z",
                distance = 3500,
                duration = 650_000,
                mapScreen = "map_screen_200",
                averageSpeed = 5.5,
                burnedCalories = 350.2
            ),
            RaceResponseBody(
                id = "300",
                date = "2023-05-13T10:12:45.555Z",
                distance = 3000,
                duration = 525_000,
                mapScreen = "map_screen_300",
                averageSpeed = 6.3,
                burnedCalories = 233.3
            )
        )
    }

    fun getRacesModel(): List<Race> {
        return getRaces().map { it.toModel() }
    }
}