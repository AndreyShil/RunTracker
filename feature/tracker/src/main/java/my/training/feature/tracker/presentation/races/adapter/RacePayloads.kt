package my.training.feature.tracker.presentation.races.adapter

import my.training.feature.tracker.data.model.RaceModel

internal sealed interface RacePayloads {
    data class OnCheckedStateChanged(val model: RaceModel) : RacePayloads
}