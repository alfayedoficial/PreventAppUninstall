package com.alfayedoficial.mydeviceadminreceiver.service

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import com.alfayedoficial.mydeviceadminreceiver.data.preference.getCounterOne
import com.alfayedoficial.mydeviceadminreceiver.data.preference.getInflatedPasswordActivity
import com.alfayedoficial.mydeviceadminreceiver.data.preference.saveCounterOne
import com.alfayedoficial.mydeviceadminreceiver.data.preference.saveInflatedPasswordActivity
import com.alfayedoficial.mydeviceadminreceiver.domain.model.ServiceResult
import com.alfayedoficial.mydeviceadminreceiver.presentation.activities.PasswordActivity
import com.alfayedoficial.mydeviceadminreceiver.service.usecase.AccessibilityUseCase
import com.alfayedoficial.mydeviceadminreceiver.service.usecase.AppInfoUseCase
import com.alfayedoficial.mydeviceadminreceiver.service.usecase.UninstallUseCase
import com.alfayedoficial.mydeviceadminreceiver.service.utils.ServiceUtils.readAllText

const val ACTION_PASSWORD_RESULT = "com.alfayedoficial.mydeviceadminreceiver.PASSWORD_RESULT"
const val PREF_SECRET_SHARED = "secret_shared_prefs"

class MyAccessibilityService : AccessibilityService() {

    private var counterOne = 0

    private val passwordResultReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (intent.action == ACTION_PASSWORD_RESULT) {
                val resultValue = intent.getIntExtra(RESULT_EXTRA, ServiceResult.FAILURE.value)
                counterOne = 0
                saveCounterOne(counterOne)
                if (resultValue == ServiceResult.SUCCESS.value) {
                    // Handle the success case
                    saveInflatedPasswordActivity(true)
                } else {
                    // Handle the failure case
                    saveInflatedPasswordActivity(false)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter(ACTION_PASSWORD_RESULT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(passwordResultReceiver, filter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(passwordResultReceiver, filter, RECEIVER_NOT_EXPORTED)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(passwordResultReceiver)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            val rootNode = rootInActiveWindow

            if (
                (AccessibilityUseCase().invoke(rootNode)
                        || AppInfoUseCase().invoke(rootNode)
                        || UninstallUseCase().invoke(rootNode))
                && !getInflatedPasswordActivity() && getCounterOne() < 1
            ) {
                // Check if the "App Info" screen for your app is opened
                inflatePasswordActivity()
            }
        }

        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            val rootNode = rootInActiveWindow
            rootNode?.let { readAllText(it) }
        }

    }

    override fun onInterrupt() {
        // Handle interruption of the service
    }

    private fun inflatePasswordActivity() {
        val passwordIntent = Intent(this, PasswordActivity::class.java)
        passwordIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(passwordIntent)
        counterOne++
        saveCounterOne(counterOne)
    }

    companion object {
        private const val RESULT_EXTRA = "result"
    }
}