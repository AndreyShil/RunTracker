package my.training.core.core_impl.data.manager

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import my.training.core.core_api.domain.manager.FirebaseStorageManager
import java.io.ByteArrayOutputStream
import javax.inject.Inject

internal class FirebaseStorageManagerImpl @Inject constructor() : FirebaseStorageManager {

    override fun uploadImage(
        bitmap: Bitmap?,
        folderName: String,
        imageName: String,
        failureListener: ((Exception) -> Unit)?,
        successfulListener: ((String) -> Unit)?
    ) {
        bitmap ?: return
        val reference = FirebaseStorage
            .getInstance()
            .reference
            .child("$folderName/$imageName")

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val bytes = outputStream.toByteArray()

        reference
            .putBytes(bytes)
            .addOnSuccessListener {
                successfulListener?.invoke(it.storage.toString())
            }
            .addOnFailureListener {
                failureListener?.invoke(it)
            }
    }

    override fun uploadImage(
        fileUri: Uri?,
        folderName: String,
        imageName: String,
        failureListener: ((Exception) -> Unit)?,
        successfulListener: ((String) -> Unit)?
    ) {
        fileUri ?: return
        val reference = FirebaseStorage
            .getInstance()
            .reference
            .child("$folderName/$imageName")

        reference
            .putFile(fileUri)
            .addOnSuccessListener {
                successfulListener?.invoke(it.storage.toString())
            }
            .addOnFailureListener {
                failureListener?.invoke(it)
            }
    }

    override fun downloadImage(
        imageUrl: String,
        successfulListener: (Uri) -> Unit
    ) {
        FirebaseStorage
            .getInstance()
            .getReferenceFromUrl(imageUrl)
            .downloadUrl
            .addOnSuccessListener {
                successfulListener(it)
            }.addOnFailureListener {
                it.printStackTrace()
            }
    }
}