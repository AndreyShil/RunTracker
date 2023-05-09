package my.training.feature.tracker.presentation.races

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

internal class RacesViewModelFactory @Inject constructor() : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RacesViewModel() as T
    }
}