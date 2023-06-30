package my.training.feature.tracker.presentation.tracker

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.training.core.core_api.domain.manager.ResourcesManager
import my.training.core.core_api.domain.model.race.RaceCreating
import my.training.core.core_api.extensions.doOnFailure
import my.training.core.core_api.extensions.doOnSuccess
import my.training.core.core_api.extensions.getCurrentDate
import my.training.core.core_api.extensions.getErrorMessage
import my.training.core.strings.R
import my.training.core.ui.base.BaseViewModel
import my.training.feature.tracker.data.repository.RaceRepository
import kotlin.math.roundToInt

internal class TrackerViewModel(
    private val raceRepository: RaceRepository,
    private val resourcesManager: ResourcesManager
) : BaseViewModel<TrackerContract.Event, TrackerContract.State, TrackerContract.Effect>() {

    override fun createInitialState(): TrackerContract.State = TrackerContract.State()

    override fun handleEvent(event: TrackerContract.Event) {
        when (event) {
            is TrackerContract.Event.OnWorkoutFinished -> {
                setState {
                    copy(
                        race = race.copy(
                            date = getCurrentDate(),
                            distance = event.distance.roundToInt(),
                            duration = event.time
                        )
                    )
                }
            }

            is TrackerContract.Event.OnMapScreenDownloaded -> {
                setState {
                    copy(
                        race = race.copy(
                            mapScreen = event.mapScreenUrl
                        )
                    )
                }
                addNewWorkout()
            }
        }
    }

    fun addNewWorkout() {
        viewModelScope.launch {
            raceRepository.createRace(currentState.race)
                .doOnSuccess {
                    setEffect {
                        TrackerContract.Effect.ShowMessage(
                            resourcesManager.getString(R.string.workout_was_added)
                        )
                    }
                    setState {
                        copy(race = RaceCreating())
                    }
                }
                .doOnFailure {
                    setEffect {
                        TrackerContract.Effect.ShowMessage(it.getErrorMessage())
                    }
                }
        }
    }
}
