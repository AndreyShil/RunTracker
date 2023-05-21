package my.training.feature.tracker.presentation.tracker

import my.training.core.core_api.data.model.race.RaceCreating
import my.training.core.ui.base.UiEffect
import my.training.core.ui.base.UiEvent
import my.training.core.ui.base.UiState

internal object TrackerContract {

    data class State(
        val race: RaceCreating = RaceCreating()
    ) : UiState

    sealed interface Event : UiEvent {
        data class OnWorkoutFinished(val distance: Double, val time: Long) : Event
        data class OnMapScreenDownloaded(val mapScreenUrl: String) : Event
    }

    sealed interface Effect : UiEffect {
        data class ShowMessage(val message: String) : Effect
    }

}