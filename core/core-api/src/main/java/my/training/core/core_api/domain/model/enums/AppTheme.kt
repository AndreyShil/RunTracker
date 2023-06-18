package my.training.core.core_api.domain.model.enums

enum class AppTheme {
    SYSTEM,
    LIGHT_MODE,
    DARK_MODE;

    companion object {
        fun getByName(name: String?): AppTheme {
            return values().firstOrNull { it.name == name } ?: SYSTEM
        }
    }
}