package my.training.feature.stats.presentation.stats_screen

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.training.core.core_api.extensions.doOnFailure
import my.training.core.core_api.extensions.doOnSuccess
import my.training.core.core_api.extensions.getErrorMessage
import my.training.core.ui.base.BaseViewModel
import my.training.feature.stats.data.StatsRepository

internal class StatsViewModel(
    private val statsRepository: StatsRepository
) : BaseViewModel<StatsContract.Event, StatsContract.State, StatsContract.Effect>() {

    val listener: (Int) -> Unit = {
        loadData(it)
    }

    override fun createInitialState(): StatsContract.State = StatsContract.State()
    override fun handleEvent(event: StatsContract.Event) = Unit

    private fun loadData(dayCount: Int) {
        setState { copy(isLoading = true) }
        viewModelScope.launch {
            statsRepository.loadStats(dayCount)
                .doOnSuccess {
                    setState {
                        copy(
                            stats = it,
                            isLoading = false
                        )
                    }
                }
                .doOnFailure {
                    setEffect {
                        StatsContract.Effect.ShowError(
                            errorMessage = it.getErrorMessage()
                        )
                    }
                    setState { copy(isLoading = false) }
                }
        }
    }
}