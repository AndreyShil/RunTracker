package my.training.core.core_impl.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import my.training.core.core_impl.data.database.dao.UserDao
import my.training.core.core_impl.data.model.dto.UserDTO

@Database(
    entities = [
        UserDTO::class
    ],
    version = AppDatabase.DB_VERSION
)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        internal const val DB_VERSION = 1
        internal const val DATABASE_NAME = "run_tracker.db"
    }
}