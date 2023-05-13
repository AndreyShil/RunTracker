package my.training.core.core_api.data.model.race

data class Race(
    val id: String,
    val date: String,
    val distance: Int,
    val duration: Long,
    val mapScreenshot: String?
)