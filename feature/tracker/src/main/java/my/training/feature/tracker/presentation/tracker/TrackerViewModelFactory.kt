package my.training.feature.tracker.presentation.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.feature.tracker.domain.RaceRepository
import javax.inject.Inject

internal class TrackerViewModelFactory @Inject constructor(
    private val raceRepository: RaceRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrackerViewModel(
            raceRepository = raceRepository
        ) as T
    }
}