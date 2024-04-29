package com.casecode.preventUninstallApp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import com.casecode.preventUninstallApp.AlarmUtils.setupAlarm
import com.casecode.preventUninstallApp.service.utils.ServiceUtils.checkAndPromptAccessibilityService

/**
 * Utility object for common Android-related tasks.
 *
 * @since 30-04-2024
 * @author Abdelaziz Daoud
 */
object AndroidUtils {

    /**
     * Checks if the app is currently ignoring battery optimizations.
     *
     * @param activity The activity context.
     * @return true if battery optimizations are ignored, false otherwise.
     */
    private fun isIgnoringBatteryOptimizations(activity: Activity): Boolean {
        // Get PowerManager system service
        val powerManager = activity.getSystemService(Context.POWER_SERVICE) as PowerManager

        // Check if battery optimizations are ignored for the app
        return powerManager.isIgnoringBatteryOptimizations(activity.packageName)
    }

    /**
     * Requests to ignore battery optimizations for the app.
     *
     * @param activity The activity context.
     */
    private fun requestIgnoreBatteryOptimizations(activity: Activity) {
        // Create an intent to request ignoring battery optimizations
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            // Set the data URI to the package name of the app
            data = Uri.parse("package:${activity.packageName}")
        }
        // Start the activity to prompt the user to ignore battery optimizations
        activity.startActivity(intent)
    }

    /**
     * Configures the Prevent Uninstall App functionality by setting up necessary components and services.
     * This includes checking if battery optimizations are ignored, initializing alarms, and prompting the user
     * to enable the accessibility service if necessary.
     *
     * @param activity The activity context in which the configuration is performed.
     */
    fun configurePreventUninstallApp(activity: Activity) {
        // Check if battery optimizations are ignored, if not, request to ignore them
        if (!isIgnoringBatteryOptimizations(activity)) {
            requestIgnoreBatteryOptimizations(activity)
        }

        // Initialize the alarm
        setupAlarm(activity)

        // Check and prompt the user to enable accessibility service
        checkAndPromptAccessibilityService(activity)
    }
}