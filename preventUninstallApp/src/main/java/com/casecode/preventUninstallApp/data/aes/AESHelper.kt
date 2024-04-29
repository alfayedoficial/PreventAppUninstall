package com.casecode.preventUninstallApp.data.aes

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * AESHelper is a utility object for AES encryption and decryption.
 *
 * @since 30-04-2024
 */
object AESHelper {

    private const val ALGORITHM = "AES"

    // Hardcoded key for encryption and decryption
    private val keyValue = byteArrayOf(
        'T'.code.toByte(),
        'h'.code.toByte(),
        'e'.code.toByte(),
        'B'.code.toByte(),
        'e'.code.toByte(),
        's'.code.toByte(),
        't'.code.toByte(),
        'S'.code.toByte(),
        'e'.code.toByte(),
        'c'.code.toByte(),
        'r'.code.toByte(),
        'e'.code.toByte(),
        't'.code.toByte(),
        'K'.code.toByte(),
        'e'.code.toByte(),
        'y'.code.toByte()
    )

    /**
     * Encrypts the given clear text using AES encryption.
     *
     * @param clearText The text to be encrypted.
     * @return The encrypted text.
     */
    fun encrypt(clearText: String): String {
        val secretKeySpec = SecretKeySpec(keyValue, ALGORITHM)
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        val encrypted = cipher.doFinal(clearText.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    /**
     * Decrypts the given encrypted text using AES decryption.
     *
     * @param encrypted The text to be decrypted.
     * @return The decrypted text.
     */
    fun decrypt(encrypted: String): String {
        val enc = Base64.decode(encrypted, Base64.DEFAULT)
        val secretKeySpec = SecretKeySpec(keyValue, ALGORITHM)
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
        val decrypted = cipher.doFinal(enc)
        return String(decrypted)
    }
}