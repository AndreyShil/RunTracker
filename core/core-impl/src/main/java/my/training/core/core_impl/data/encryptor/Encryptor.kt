package my.training.core.core_impl.data.encryptor

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import my.training.core.core_api.di.qualifiers.AppContext
import my.training.core.core_api.extensions.getDeviceId
import java.security.KeyStore
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

internal class Encryptor @Inject constructor(
    @AppContext private val appContext: Context
) {

    private val ivSpec by lazy { getInitializationVector() }

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), ivSpec)
        val encodedBytes = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encodedBytes, Base64.NO_WRAP)
    }

    fun decrypt(encrypted: String): String {
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), ivSpec)
        val encodedBytes = Base64.decode(encrypted, Base64.NO_WRAP)
        val decodedBytes = cipher.doFinal(encodedBytes)
        return String(decodedBytes, Charsets.UTF_8)
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
        if (keyStore.getEntry(KEY_ALIAS, null) != null) {
            val secretKeyEntry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry
            return secretKeyEntry.secretKey ?: generateSecretKey()
        }
        return generateSecretKey()
    }

    private fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEY_STORE
        )
        keyGenerator.init(getKeyGenParameterSpec())
        return keyGenerator.generateKey()
    }

    private fun getKeyGenParameterSpec(): KeyGenParameterSpec {
        val purposes = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        return KeyGenParameterSpec.Builder(KEY_ALIAS, purposes)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setRandomizedEncryptionRequired(false)
            .build()
    }


    private fun getInitializationVector(): AlgorithmParameterSpec {
        val deviceID = appContext.getDeviceId()
        return IvParameterSpec(deviceID.toByteArray())
    }

    companion object {
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val AES_MODE = "AES/CBC/PKCS7Padding"
        private const val KEY_ALIAS = "key_store_alias"
    }
}