package com.alfayedoficial.mydeviceadminreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences

import androidx.security.crypto.MasterKey




class UpdatePreferenceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        context.saveInflatedPasswordActivity( false)
    }
}

fun Context.saveInflatedPasswordActivity(value: Boolean) {
    val editor = getSharedPreferences().edit()
    editor.putBoolean(INFLATE_PASSWORD_ACTIVITY, value)
    editor.apply()
}

fun Context.saveCounterOne(value: Int) {
    val editor = getSharedPreferences().edit()
    editor.putInt(COUNTER_ONE, value)
    editor.apply()
}

fun Context.getInflatedPasswordActivity(): Boolean {
    return getSharedPreferences().getBoolean(INFLATE_PASSWORD_ACTIVITY, false)
}

fun Context.getCounterOne(): Int {
    return getSharedPreferences().getInt(COUNTER_ONE, 0)
}

fun Context.getSharedPreferences(): SharedPreferences {
    val masterKey = MasterKey.Builder(this)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    return EncryptedSharedPreferences.create(
        this,
        MY_SHARED_PREF,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}


fun Context.savePassword(password: String) {
    val editor = getSharedPreferences().edit()
    val encryptedPassword = AESHelper.encrypt(password)
    editor.putString(PASSWORD, encryptedPassword)
    editor.apply()
}

fun Context.getDecryptPassword(): String {
    val encryptedPassword = getSharedPreferences().getString(PASSWORD, "")
    return if (!encryptedPassword.isNullOrEmpty()) {
        AESHelper.decrypt(encryptedPassword)
    } else {
        ""
    }
}


