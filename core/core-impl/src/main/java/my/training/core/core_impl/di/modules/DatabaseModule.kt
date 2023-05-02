package my.training.core.core_impl.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import my.training.core.core_api.di.qualifiers.AppContext
import my.training.core.core_api.di.scopes.PerApplication
import my.training.core.core_impl.data.database.AppDatabase
import my.training.core.core_impl.data.database.dao.UserDao

@Module
internal object DatabaseModule {

    @Provides
    @PerApplication
    fun provideDatabase(@AppContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()
}