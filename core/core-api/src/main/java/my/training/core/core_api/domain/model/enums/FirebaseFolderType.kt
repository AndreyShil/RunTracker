package my.training.core.core_api.domain.model.enums

private const val AVATARS_FOLDER_NAME = "avatars"
private const val RACES_FOLDER_NAME = "races"

enum class FirebaseFolderType {
    AVATARS,
    RACES;

    fun getFolderName(): String {
        return when(this) {
            AVATARS -> AVATARS_FOLDER_NAME
            RACES -> RACES_FOLDER_NAME
        }
    }
}