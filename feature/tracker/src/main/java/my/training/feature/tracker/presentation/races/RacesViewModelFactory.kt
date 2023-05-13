package my.training.feature.tracker.presentation.races

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.feature.tracker.domain.use_case.GetRacesUseCase
import javax.inject.Inject

internal class RacesViewModelFactory @Inject constructor(
    private val getRacesUseCase: GetRacesUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RacesViewModel(
            getRaces = getRacesUseCase
        ) as T
    }
}