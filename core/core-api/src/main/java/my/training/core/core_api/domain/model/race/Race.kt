package my.training.core.core_api.domain.model.race

data class Race(
    val id: String = "",
    val date: String = "",
    val distance: Int = 0,
    val duration: Long = 0L,
    val mapScreen: String? = null,
    val averageSpeed: Double = 0.0,
    val burnedCalories: Double = 0.0
)
