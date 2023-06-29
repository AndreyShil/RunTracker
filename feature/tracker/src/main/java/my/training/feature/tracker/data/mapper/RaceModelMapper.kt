package my.training.feature.tracker.data.mapper

import my.training.core.core_api.domain.model.race.Race
import my.training.core.core_api.extensions.getFormattedDate
import my.training.feature.tracker.data.model.RaceModel

internal fun List<Race>.toRaceModelList(): List<RaceModel> {
    val raceGroup = this.groupBy { it.date.getFormattedDate() }
    return buildList {
        raceGroup.forEach { (date, races) ->
            add(
                RaceModel.DateHeader(
                    date = date
                )
            )
            addAll(
                races.map { RaceModel.RaceInfo(it) }
            )
        }
    }
}