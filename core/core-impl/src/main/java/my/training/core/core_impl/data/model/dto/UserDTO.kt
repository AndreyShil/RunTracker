package my.training.core.core_impl.data.model.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("users")
internal data class UserDTO(

    @PrimaryKey
    @ColumnInfo("login")
    val login: String = "",

    @ColumnInfo("first_name")
    val firstName: String? = null,

    @ColumnInfo("last_name")
    val lastName: String? = null,

    @ColumnInfo("email")
    val email: String? = null,

    @ColumnInfo("photo")
    val photo: String? = null
)