package my.training.core.core_api.data.model.user

data class User(
    val login: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val photo: String? = null
) {

    fun getFullName(): String {
        return buildString {
            if (!firstName.isNullOrEmpty()) {
                append(firstName)
            }
            if (isHasFullNameData()) {
                append(" ")
            }
            if (!lastName.isNullOrEmpty()) {
                append(lastName)
            }
        }
    }

    private fun isHasFullNameData(): Boolean {
        return !firstName.isNullOrEmpty() && !lastName.isNullOrEmpty()
    }
}