package my.training.feature.tracker.presentation.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.core.core_api.domain.manager.ResourcesManager
import my.training.feature.tracker.data.repository.RaceRepository
import javax.inject.Inject

internal class TrackerViewModelFactory @Inject constructor(
    private val raceRepository: RaceRepository,
    private val resourcesManager: ResourcesManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrackerViewModel(
            raceRepository = raceRepository,
            resourcesManager = resourcesManager
        ) as T
    }
}