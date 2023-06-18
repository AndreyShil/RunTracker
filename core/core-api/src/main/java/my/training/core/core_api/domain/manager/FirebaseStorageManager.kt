package my.training.core.core_api.domain.manager

import android.graphics.Bitmap
import android.net.Uri
import my.training.core.core_api.domain.model.enums.FirebaseFolderType

interface FirebaseStorageManager {

    fun uploadImage(
        bitmap: Bitmap?,
        folderType: FirebaseFolderType,
        imageName: String,
        failureListener: ((Exception) -> Unit)? = null,
        successfulListener: ((String) -> Unit)? = null
    )

    fun uploadImage(
        fileUri: Uri?,
        folderType: FirebaseFolderType,
        imageName: String,
        failureListener: ((Exception) -> Unit)? = null,
        successfulListener: ((String) -> Unit)? = null
    )

    fun downloadImage(
        imageUrl: String,
        successfulListener: (Uri) -> Unit
    )
}