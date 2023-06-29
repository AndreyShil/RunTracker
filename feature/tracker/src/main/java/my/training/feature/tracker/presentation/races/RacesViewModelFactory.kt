package my.training.feature.tracker.presentation.races

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.feature.tracker.data.repository.RaceRepository
import javax.inject.Inject

internal class RacesViewModelFactory @Inject constructor(
    private val raceRepository: RaceRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RacesViewModel(
            raceRepository = raceRepository
        ) as T
    }
}