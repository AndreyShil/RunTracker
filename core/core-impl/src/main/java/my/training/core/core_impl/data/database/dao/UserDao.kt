package my.training.core.core_impl.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import my.training.core.core_impl.data.model.dto.UserDTO

@Dao
internal interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserDTO)

    @Query("SELECT * FROM users")
    fun getUserFlow(): Flow<UserDTO>

    @Query("DELETE FROM users")
    suspend fun clear()
}