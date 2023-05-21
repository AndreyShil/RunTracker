package my.training.core.core_api.domain.manager

import android.graphics.Bitmap
import android.net.Uri

interface FirebaseStorageManager {

    fun uploadImage(
        bitmap: Bitmap?,
        folderName: String,
        imageName: String,
        failureListener: ((Exception) -> Unit)? = null,
        successfulListener: ((String) -> Unit)? = null
    )

    fun uploadImage(
        fileUri: Uri?,
        folderName: String,
        imageName: String,
        failureListener: ((Exception) -> Unit)? = null,
        successfulListener: ((String) -> Unit)? = null
    )

    fun downloadImage(
        imageUrl: String,
        successfulListener: (Uri) -> Unit
    )
}