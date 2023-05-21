package my.training.core.core_api.di

import my.training.core.core_api.domain.manager.FirebaseStorageManager

interface FirebaseStorageProvider {
    fun provideFirebaseStorageManager(): FirebaseStorageManager
}