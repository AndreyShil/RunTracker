package my.training.core.core_impl.di.modules

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import my.training.core.core_api.di.qualifiers.AppContext
import my.training.core.core_api.di.scopes.PerApplication
import my.training.core.core_api.domain.preferences.AppSettings
import my.training.core.core_api.domain.preferences.Preferences
import my.training.core.core_impl.data.preferences.AppSettingsAboveSImpl
import my.training.core.core_impl.data.preferences.AppSettingsBelowSImpl
import my.training.core.core_impl.data.preferences.PreferencesImpl

@Module
internal interface PreferencesModule {

    @Binds
    @PerApplication
    fun bindPreferences(impl: PreferencesImpl): Preferences

    companion object {

        private const val SECURE_PREFS_NAME = "secure_prefs"
        private val keyScheme = MasterKey.KeyScheme.AES256_GCM

        @Provides
        @PerApplication
        fun provideEncryptedPreferences(
            @AppContext appContext: Context
        ): SharedPreferences {
            return EncryptedSharedPreferences.create(
                appContext,
                SECURE_PREFS_NAME,
                MasterKey.Builder(appContext).setKeyScheme(keyScheme).build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        @Provides
        @PerApplication
        fun provideAppSettings(
            @AppContext appContext: Context,
            preferences: Preferences
        ): AppSettings {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                AppSettingsAboveSImpl(appContext, preferences)
            } else {
                AppSettingsBelowSImpl(preferences)
            }
        }
    }
}