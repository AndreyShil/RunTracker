package my.training.feature.tracker.presentation.races

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.training.core.core_api.domain.model.race.Race
import my.training.core.core_api.extensions.doOnFailure
import my.training.core.core_api.extensions.doOnSuccess
import my.training.core.core_api.extensions.getErrorMessage
import my.training.core.core_api.extensions.getFormattedDate
import my.training.core.ui.base.BaseViewModel
import my.training.feature.tracker.domain.RaceRepository
import my.training.feature.tracker.domain.model.RaceModel

internal class RacesViewModel(
    private val raceRepository: RaceRepository
) : BaseViewModel<RacesContract.Event, RacesContract.State, RacesContract.Effect>() {

    private var raceItems = emptyList<RaceModel>()

    override fun createInitialState(): RacesContract.State {
        return RacesContract.State()
    }

    override fun handleEvent(event: RacesContract.Event) {

    }

    init {
        viewModelScope.launch {
            raceRepository.getRaces()
                .doOnSuccess { races ->
                    raceItems = races.toRaceModelList()
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

    private fun List<Race>.toRaceModelList(): List<RaceModel> {
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

    fun updateRacesList(raceId: String) {
        val mRaces = raceItems.toMutableList()
        val targetRace = mRaces.find { it.getItemId() == raceId }
        if (targetRace is RaceModel.RaceInfo) {
            val index = mRaces.indexOf(targetRace)
            mRaces[index] = targetRace.copy(isExpanded = !targetRace.isExpanded)
            raceItems = mRaces.toList()
            setState { copy(races = raceItems) }
        }
    }
}