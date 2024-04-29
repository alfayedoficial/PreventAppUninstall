package com.casecode.preventUninstallApp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.casecode.preventUninstallApp.data.preference.UpdatePreferenceReceiver

/**
 * Utility object for managing alarm-related tasks.
 *
 * @since 30-04-2024
 * @author Abdelaziz Daoud
 */
object AlarmUtils {

    /**
     * Sets up an alarm to trigger periodic updates.
     *
     * @param context The context used to access system services and resources.
     */
    fun setupAlarm(context: Context) {
        // Obtain the AlarmManager system service
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Create an intent to specify the broadcast receiver class that will handle the alarm
        val intent = Intent(context, UpdatePreferenceReceiver::class.java)

        // Create a PendingIntent to be triggered by the alarm
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Define the interval for the alarm (1 minute in milliseconds)
        val interval = 60000L

        // Set the alarm to trigger periodically at the specified interval
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + interval,
            interval,
            pendingIntent
        )
    }
}