package my.training.feature.tracker.presentation.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.feature.tracker.domain.use_case.CreateWorkoutUseCase
import javax.inject.Inject

internal class TrackerViewModelFactory @Inject constructor(
    private val createWorkoutUseCase: CreateWorkoutUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrackerViewModel(
            createWorkout = createWorkoutUseCase
        ) as T
    }
}