package my.training.core.core_impl.mapper

import my.training.core.core_api.data.model.race.Race
import my.training.core.core_impl.data.model.response.RaceResponseBody

internal fun RaceResponseBody.toModel(): Race {
    return Race(
        id = id ?: "",
        date = date ?: "",
        distance = distance ?: 0,
        duration = duration ?: 0L,
        mapScreenshot = mapScreenshot
    )
}