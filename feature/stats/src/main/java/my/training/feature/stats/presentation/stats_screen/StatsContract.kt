package my.training.feature.stats.presentation.stats_screen

import my.training.core.ui.base.UiEffect
import my.training.core.ui.base.UiEvent
import my.training.core.ui.base.UiState
import my.training.feature.stats.data.model.Stats

internal object StatsContract {

    data class State(
        val stats: List<Stats> = emptyList(),
        val isLoading: Boolean = false
    ) : UiState

    sealed interface Event : UiEvent {

    }

    sealed interface Effect : UiEffect {
        data class ShowError(val errorMessage: String) : Effect
    }

}