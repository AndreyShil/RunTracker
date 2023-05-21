package my.training.feature.tracker.presentation.races

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.training.core.core_api.extensions.doOnFailure
import my.training.core.core_api.extensions.doOnSuccess
import my.training.core.core_api.extensions.getErrorMessage
import my.training.core.ui.base.BaseViewModel
import my.training.feature.tracker.data.RaceModel
import my.training.feature.tracker.domain.use_case.GetRacesUseCase

internal class RacesViewModel(
    private val getRaces: GetRacesUseCase
) : BaseViewModel<RacesContract.Event, RacesContract.State, RacesContract.Effect>() {

    private var raceItems = emptyList<RaceModel>()

    override fun createInitialState(): RacesContract.State {
        return RacesContract.State()
    }

    override fun handleEvent(event: RacesContract.Event) {

    }

    init {
        viewModelScope.launch {
            getRaces()
                .doOnSuccess { races ->
                    raceItems = races.map { RaceModel(it) }
                    setState { copy(races = raceItems) }
                }
                .doOnFailure {
                    setEffect {
                        RacesContract.Effect.ShowError(
                            errorMessage = it.getErrorMessage()
                        )
                    }
                }
        }
    }

    fun updateRacesList(raceId: String) {
        val mRaces = raceItems.toMutableList()
        val targetRace = mRaces.find { it.race.id == raceId }
        if (targetRace != null) {
            val index = mRaces.indexOf(targetRace)
            mRaces[index] = targetRace.copy(isExpanded = !targetRace.isExpanded)
            raceItems = mRaces.toList()
            setState { copy(races = raceItems) }
        }
    }
}