package my.training.feature.stats.domain

import my.training.core.core_api.data.model.NetworkResponse
import my.training.feature.stats.domain.model.Stats

internal interface StatsRepository {

    suspend fun loadStats(dayCount: Int): NetworkResponse<List<Stats>>
}