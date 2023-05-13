package my.training.feature.tracker.presentation.races

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.training.core.core_api.extensions.doOnFailure
import my.training.core.core_api.extensions.doOnSuccess
import my.training.core.core_api.extensions.getErrorMessage
import my.training.core.ui.base.BaseViewModel
import my.training.feature.tracker.domain.use_case.GetRacesUseCase

internal class RacesViewModel(
    private val getRaces: GetRacesUseCase
) : BaseViewModel<RacesContract.Event, RacesContract.State, RacesContract.Effect>() {

    override fun createInitialState(): RacesContract.State {
        return RacesContract.State()
    }

    override fun handleEvent(event: RacesContract.Event) {

    }

    init {
        viewModelScope.launch {
            getRaces()
                .doOnSuccess {
                    setState {
                        copy(races = it)
                    }
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
}