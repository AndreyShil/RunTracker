package my.training.core.core_api

import my.training.core.core_api.domain.model.race.Race
import my.training.core.core_api.domain.model.race.RaceCreating
import my.training.core.core_api.data.model.request.race.RaceRequestBody
import my.training.core.core_api.data.model.response.race.RaceResponseBody

fun RaceResponseBody.toModel(): Race {
    return Race(
        id = id ?: "",
        date = date ?: "",
        distance = distance ?: 0,
        duration = duration ?: 0L,
        mapScreen = mapScreen,
        averageSpeed = averageSpeed ?: 0.0,
        burnedCalories = burnedCalories ?: 0.0
    )
}

fun RaceCreating.toRequestBody(): RaceRequestBody {
    return RaceRequestBody(
        date = date,
        distance = distance,
        duration = duration,
        mapScreen = mapScreen
    )
}
