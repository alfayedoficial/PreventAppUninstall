package com.casecode.preventUninstallApp.service

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import com.casecode.preventUninstallApp.data.ACTION_PASSWORD_RESULT
import com.casecode.preventUninstallApp.data.preference.getCounterOne
import com.casecode.preventUninstallApp.data.preference.getInflatedPasswordActivity
import com.casecode.preventUninstallApp.data.preference.saveCounterOne
import com.casecode.preventUninstallApp.presentation.activities.PasswordActivity
import com.casecode.preventUninstallApp.service.usecase.AccessibilityUseCase
import com.casecode.preventUninstallApp.service.usecase.AppInfoUseCase
import com.casecode.preventUninstallApp.service.usecase.UninstallUseCase
import com.casecode.preventUninstallApp.service.utils.ServiceUtils.readAllText

/**
 * MyAccessibilityService is an AccessibilityService responsible for handling accessibility events
 * and performing specific actions based on those events.
 */
class MyAccessibilityService : AccessibilityService() {

    private var counterOne = 0

    /**
     * BroadcastReceiver for receiving password results.
     */
    private val passwordResultReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {}
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter(ACTION_PASSWORD_RESULT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(passwordResultReceiver, filter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(passwordResultReceiver, filter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(passwordResultReceiver)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            val rootNode = rootInActiveWindow

            // Check if the (Accessibility, App Info, Uninstall) screen for your app is opened
            val isAccessibility = AccessibilityUseCase().invoke(rootNode)
            val isAppInfo = AppInfoUseCase().invoke(rootNode)
            val isUninstall = UninstallUseCase().invoke(rootNode)
            val isCounterOne = getCounterOne() < 1

            if ((isAccessibility || isAppInfo || isUninstall) &&
                !getInflatedPasswordActivity() && isCounterOne
            ) {
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

    /**
     * Inflates the PasswordActivity and increments the counter.
     */
    private fun inflatePasswordActivity() {
        val passwordIntent = Intent(this, PasswordActivity::class.java)
        passwordIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(passwordIntent)
        counterOne++
        saveCounterOne(counterOne)
    }
}