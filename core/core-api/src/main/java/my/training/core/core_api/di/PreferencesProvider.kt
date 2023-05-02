package my.training.core.core_api.di

import my.training.core.core_api.domain.preferences.Preferences

interface PreferencesProvider {
    fun providePreferences(): Preferences
}