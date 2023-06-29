package my.training.feature.tracker.presentation.races

import my.training.core.ui.base.UiEffect
import my.training.core.ui.base.UiEvent
import my.training.core.ui.base.UiState
import my.training.feature.tracker.data.model.RaceModel

internal object RacesContract {

    data class State(
        val races: List<RaceModel> = emptyList(),
        val isLoading: Boolean = false
    ) : UiState

    sealed interface Event : UiEvent {
        data class UpdateRacesList(val raceId: String) : Event
    }

    sealed interface Effect : UiEffect {
        data class ShowError(val errorMessage: String) : Effect
    }

}