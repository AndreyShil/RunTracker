package my.training.feature.tracker.presentation.races

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.training.core.core_api.extensions.doOnFailure
import my.training.core.core_api.extensions.doOnSuccess
import my.training.core.core_api.extensions.getErrorMessage
import my.training.core.ui.base.BaseViewModel
import my.training.feature.tracker.data.mapper.toRaceModelList
import my.training.feature.tracker.data.model.RaceModel
import my.training.feature.tracker.data.repository.RaceRepository

internal class RacesViewModel(
    private val raceRepository: RaceRepository
) : BaseViewModel<RacesContract.Event, RacesContract.State, RacesContract.Effect>() {

    override fun createInitialState(): RacesContract.State = RacesContract.State()

    override fun handleEvent(event: RacesContract.Event) {
        when (event) {
            is RacesContract.Event.UpdateRacesList -> {
                updateRacesList(event.raceId)
            }
        }
    }

    init {
        loadRaces()
    }

    fun loadRaces() {
        viewModelScope.launch {
            raceRepository.getRaces()
                .doOnSuccess { races ->
                    val raceItems = races.toRaceModelList()
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

    private fun updateRacesList(raceId: String) {
        val mRaces = currentState.races.toMutableList()
        val targetRace = mRaces.find { it.getItemId() == raceId }
        if (targetRace is RaceModel.RaceInfo) {
            val index = mRaces.indexOf(targetRace)
            mRaces[index] = targetRace.copy(isExpanded = !targetRace.isExpanded)
            val raceItems = mRaces.toList()
            setState { copy(races = raceItems) }
        }
    }
}
