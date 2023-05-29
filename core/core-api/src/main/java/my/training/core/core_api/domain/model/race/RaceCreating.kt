package my.training.core.core_api.domain.model.race

data class RaceCreating(
    val date: String = "",
    val distance: Int = 0,
    val duration: Long = 0L,
    val mapScreen: String? = null
)
