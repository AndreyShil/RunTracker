package my.training.core.core_impl.di.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Binds
import dagger.Module
import dagger.Provides
import my.training.core.core_api.di.qualifiers.AppContext
import my.training.core.core_api.di.scopes.PerApplication
import my.training.core.core_api.domain.preferences.Preferences
import my.training.core.core_impl.data.preferences.PreferencesImpl

@Module
internal interface PreferencesModule {

    @Binds
    @PerApplication
    fun bindPreferences(impl: PreferencesImpl): Preferences

    companion object {

        private const val SECURE_PREFS_NAME = "secure_prefs"
        private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        @Provides
        @PerApplication
        fun provideEncryptedPreferences(
            @AppContext appContext: Context
        ): SharedPreferences {
            return EncryptedSharedPreferences.create(
                SECURE_PREFS_NAME,
                mainKeyAlias,
                appContext,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }
}