package my.training.feature.tracker.data

import my.training.core.core_api.data.model.race.Race

internal data class RaceModel(
    val race: Race,
    val isExpanded: Boolean = false
)