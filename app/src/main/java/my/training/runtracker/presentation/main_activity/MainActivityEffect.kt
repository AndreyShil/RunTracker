package my.training.runtracker.presentation.main_activity

sealed interface MainActivityEffect {
    object OpenAuthGraph : MainActivityEffect
    object OpenMainGraph : MainActivityEffect
    data class ShowError(val errorMessage: String): MainActivityEffect
}