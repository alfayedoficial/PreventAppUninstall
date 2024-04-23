package com.alfayedoficial.mydeviceadminreceiver

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AESHelper {
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
    fun encrypt(clearText: String): String {
        val skeySpec = SecretKeySpec(keyValue, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
        val encrypted = cipher.doFinal(clearText.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    fun decrypt(encrypted: String): String {
        val enc = Base64.decode(encrypted, Base64.DEFAULT)
        val skeySpec = SecretKeySpec(keyValue, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec)
        val decrypted = cipher.doFinal(enc)
        return String(decrypted)
    }
}