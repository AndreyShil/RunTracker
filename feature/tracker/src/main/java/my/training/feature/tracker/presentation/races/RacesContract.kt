package my.training.feature.tracker.presentation.races

import my.training.core.core_api.data.model.race.Race
import my.training.core.ui.base.UiEffect
import my.training.core.ui.base.UiEvent
import my.training.core.ui.base.UiState

internal object RacesContract {

    data class State(
        val races: List<Race> = emptyList(),
        val isLoading: Boolean = false
    ) : UiState

    sealed interface Event : UiEvent {

    }

    sealed interface Effect : UiEffect {
        data class ShowError(val errorMessage: String) : Effect
    }

}