package com.casecode.preventUninstallApp.data.preference

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.casecode.preventUninstallApp.data.EXTRA_INFLATED_PASSWORD_ACTIVITY
import com.casecode.preventUninstallApp.data.PREF_COUNTER_ONE
import com.casecode.preventUninstallApp.data.PREF_PASSWORD
import com.casecode.preventUninstallApp.data.PREF_SECRET_SHARED
import com.casecode.preventUninstallApp.data.aes.AESHelper

/**
 * BroadcastReceiver to handle preference updates.
 *
 * @since 30-04-2024
 */
class UpdatePreferenceReceiver : BroadcastReceiver() {

    /**
     * Handles receiving intent to update preferences.
     *
     * @param context The context.
     * @param intent The received intent.
     */
    override fun onReceive(context: Context, intent: Intent?) {
        context.saveInflatedPasswordActivity(false)
    }
}

/**
 * Saves the state of inflated password activity to SharedPreferences.
 *
 * @param value The boolean value to save.
 */
fun Context.saveInflatedPasswordActivity(value: Boolean) {
    getSharedPreferences().edit().apply {
        putBoolean(EXTRA_INFLATED_PASSWORD_ACTIVITY, value)
        apply()
    }
}

/**
 * Saves an integer value to SharedPreferences.
 *
 * @param value The integer value to save.
 */
fun Context.saveCounterOne(value: Int) {
    getSharedPreferences().edit().apply {
        putInt(PREF_COUNTER_ONE, value)
        apply()
    }
}

/**
 * Retrieves the state of inflated password activity from SharedPreferences.
 *
 * @return The boolean value indicating whether the password activity is inflated.
 */
fun Context.getInflatedPasswordActivity() =
    getSharedPreferences().getBoolean(EXTRA_INFLATED_PASSWORD_ACTIVITY, false)

/**
 * Retrieves the value of counter one from SharedPreferences.
 *
 * @return The value of counter one.
 */
fun Context.getCounterOne() = getSharedPreferences().getInt(PREF_COUNTER_ONE, 0)

/**
 * Retrieves the SharedPreferences instance.
 *
 * @return The SharedPreferences instance.
 */
fun Context.getSharedPreferences() = EncryptedSharedPreferences.create(
    this,
    PREF_SECRET_SHARED,
    MasterKey.Builder(this)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build(),
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)

/**
 * Saves the password securely to SharedPreferences.
 *
 * @param password The password to save.
 */
fun Context.savePassword(password: String) {
    getSharedPreferences().edit().apply {
        val encryptedPassword = AESHelper.encrypt(password)
        putString(PREF_PASSWORD, encryptedPassword)
        apply()
    }
}

/**
 * Retrieves and decrypts the saved password from SharedPreferences.
 *
 * @return The decrypted password.
 */
fun Context.getDecryptPassword() = getSharedPreferences().getString(PREF_PASSWORD, "")
    ?.takeIf { it.isNotEmpty() }
    ?.let { AESHelper.decrypt(it) }
    ?: ""