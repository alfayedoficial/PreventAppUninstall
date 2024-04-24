package com.alfayedoficial.mydeviceadminreceiver.presentation.activities

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.alfayedoficial.mydeviceadminreceiver.presentation.screens.SubmitLockPasswordScreen
import com.alfayedoficial.mydeviceadminreceiver.data.preference.UpdatePreferenceReceiver
import com.alfayedoficial.mydeviceadminreceiver.service.MyAccessibilityService
import com.alfayedoficial.mydeviceadminreceiver.ui.theme.MyDeviceAdminReceiverTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyDeviceAdminReceiverTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SubmitLockPasswordScreen {
                        if (!isIgnoringBatteryOptimizations()) {
                            requestIgnoreBatteryOptimizations()
                        }

                        // Initialize the alarm
                        setupAlarm(this)

                        checkAndPromptAccessibilityService(this)
                    }
                }
            }
        }
    }

    private fun isIgnoringBatteryOptimizations(): Boolean {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(packageName)
    }

    private fun requestIgnoreBatteryOptimizations() {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = Uri.parse("package:$packageName")
        }
        startActivity(intent)
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityManager = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN)
        val serviceName = ComponentName(this, MyAccessibilityService::class.java)
        for (service in enabledServices) {
            if (serviceName == ComponentName.unflattenFromString(service.id)) {
                return true
            }
        }
        return false
    }

    private fun openAccessibilitySettings(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    }

    private fun checkAndPromptAccessibilityService(context: Context) {
        if (!isAccessibilityServiceEnabled()) {
            // Prompt the user to enable the accessibility service
            openAccessibilitySettings(context)
        }
    }

    private fun setupAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, UpdatePreferenceReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Set the alarm to wake up the device every 10 minutes
        // 10 minutes in milliseconds
        val interval = 60000L

        // Use setInexactRepeating for battery efficiency or setExactAndAllowWhileIdle for precise scheduling
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + interval,
            interval,
            pendingIntent
        )
    }
}
