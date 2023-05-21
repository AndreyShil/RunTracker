package my.training.core.core_impl.mapper

import my.training.core.core_api.data.model.race.RaceCreating
import my.training.core.core_api.data.model.race.Race
import my.training.core.core_impl.data.model.request.race.RaceRequestBody
import my.training.core.core_impl.data.model.response.race.RaceResponseBody

internal fun RaceResponseBody.toModel(): Race {
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

internal fun RaceCreating.toRequestBody(): RaceRequestBody {
    return RaceRequestBody(
        date = date,
        distance = distance,
        duration = duration,
        mapScreen = mapScreen
    )
}
