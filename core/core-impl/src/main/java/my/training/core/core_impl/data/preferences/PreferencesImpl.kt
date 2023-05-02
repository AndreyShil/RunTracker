package my.training.core.core_impl.data.preferences

import android.content.SharedPreferences
import my.training.core.core_api.domain.preferences.Preferences
import my.training.core.core_impl.data.encryptor.Encryptor
import javax.inject.Inject

internal class PreferencesImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val encryptor: Encryptor
) : Preferences {

    private val editor = sharedPreferences.edit()

    override fun saveAccessToken(token: String) {
        editor.putString(ACCESS_KEY, encryptor.encrypt(token)).apply()
    }

    override fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_KEY, null)?.let {
            encryptor.decrypt(it)
        }
    }

    override fun clear() {
        editor.clear().apply()
    }

    companion object {
        private const val ACCESS_KEY = "access_key"
    }
}