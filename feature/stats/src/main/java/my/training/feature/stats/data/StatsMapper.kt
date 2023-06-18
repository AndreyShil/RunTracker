package my.training.feature.stats.data

import my.training.core.core_api.data.model.response.race.StatsResponseBody
import my.training.feature.stats.domain.model.Stats

internal fun StatsResponseBody.toModel(): Stats {
    return Stats(
        date = date,
        distance = distance,
        burnedCalories = burnedCalories,
        averageSpeed = averageSpeed
    )
}