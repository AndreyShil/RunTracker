package my.training.feature.tracker.presentation.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

internal class TrackerViewModelFactory @Inject constructor() : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrackerViewModel() as T
    }
}