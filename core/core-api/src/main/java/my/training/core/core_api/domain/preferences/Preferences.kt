package my.training.core.core_api.domain.preferences

interface Preferences {

    fun saveAccessToken(token: String)
    fun getAccessToken(): String?

    fun clear()
}