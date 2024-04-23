package com.alfayedoficial.mydeviceadminreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class PackageChangeReceiver(private val onPackageOpened: () -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val packageName = intent?.data?.schemeSpecificPart
        // Check if the specific package name is opened in the settings
        if (packageName == "com.android.settings") {
            onPackageOpened()
        }
    }
}