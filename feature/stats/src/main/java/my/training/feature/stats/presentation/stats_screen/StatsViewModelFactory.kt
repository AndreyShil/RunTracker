package my.training.feature.stats.presentation.stats_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.training.feature.stats.data.StatsRepository
import javax.inject.Inject

internal class StatsViewModelFactory @Inject constructor(
    private val statsRepository: StatsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatsViewModel(
            statsRepository = statsRepository
        ) as T
    }
}