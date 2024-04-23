package com.alfayedoficial.mydeviceadminreceiver

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import java.util.LinkedList

class MyAccessibilityService : AccessibilityService() {

    private var widgetView: View? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {


        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            val rootNode = rootInActiveWindow
            if (rootNode != null && isAppInfoScreen(rootNode)) {
                // Check if the "App Info" screen for your app is opened
                if (widgetView == null) {
                    if (hasSystemAlertWindowPermission()) {
                        showWidget2()
                    } else {
                        requestSystemAlertWindowPermission()
                    }
                }
            }
        }
    }

    //TAG_Log
    private fun isAppInfoScreen(rootNode: AccessibilityNodeInfo?): Boolean {
        if (rootNode == null) return false

        // Check if the screen title contains "App Info"
//        if (isAppInfoTitle(rootNode)) {
//            return true
//        }

        // Check if there's a button with the label "Force stop" (or any other relevant button)
//        if (hasForceStopButton(rootNode)) {
//            return true
//        }

        if (hasAppName(rootNode)) {
            return true
        }

        // Check if there's any other characteristic specific to the "App Info" screen for your app

        // If none of the above conditions are met, it's not the "App Info" screen for your app
        return false
    }

    private fun isAppInfoTitle(rootNode: AccessibilityNodeInfo): Boolean {
        // Search for a TextView with the text "App Info" or any other relevant title
        val titleNode = findNodeByText(rootNode, "App info")
        return titleNode != null
    }

    private fun hasForceStopButton(rootNode: AccessibilityNodeInfo): Boolean {
        // Search for a Button with the text "Force stop" or any other relevant button
        val forceStopButtonNode = findNodeByText(rootNode, "Force stop")
        return forceStopButtonNode != null
    }

    private fun hasAppName(rootNode: AccessibilityNodeInfo): Boolean {
        // Search for a Button with the text "Force stop" or any other relevant button
        val forceStopButtonNode = findNodeByText(rootNode, "MyDeviceAdminReceiver")
        return forceStopButtonNode != null
    }

    private fun findNodeByText(rootNode: AccessibilityNodeInfo, searchText: String): AccessibilityNodeInfo? {
        val nodeQueue = LinkedList<AccessibilityNodeInfo>()
        nodeQueue.add(rootNode)

        while (nodeQueue.isNotEmpty()) {
            val currentNode = nodeQueue.poll()
            if (currentNode != null && currentNode.text != null && currentNode.text.contains(searchText)) {
                return currentNode
            }

            val childCount = currentNode?.childCount ?: 0
            for (i in 0 until childCount) {
                val childNode = currentNode?.getChild(i)
                if (childNode != null) {
                    nodeQueue.add(childNode)
                }
            }
        }

        return null
    }


    private fun logAppInfoScreenEvent() {
        // Get the current time
        val currentTimeMillis = System.currentTimeMillis()

        // Get the name of your app
        val appName = getString(R.string.app_name) // Replace R.string.app_name with the actual resource ID of your app name

        // Log the event
        Log.d("TAG_Log", "App Info screen for $appName opened at $currentTimeMillis")
    }


    override fun onInterrupt() {
        // Handle interruption of the service

    }


    private fun showWidget2() {
        if (!isWidgetShown()) {
            val intent = Intent(applicationContext, WidgetActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)
        }
    }



    private fun showWidget() {
        if (!isWidgetShown()) {
            val context = applicationContext
            val view = ComposeView(context).apply {
                setContent {
                    WidgetContent()
                }
            }

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )

            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.addView(view, params)
            widgetView = view

            logAppInfoScreenEvent()
        }
    }

    fun hideWidget() {
        if (isWidgetShown()) {
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.removeView(widgetView)
            widgetView = null
        }
    }

    private fun isWidgetShown(): Boolean {
        return widgetView != null
    }

    private fun hasSystemAlertWindowPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true // No need for permission before Android 6.0
        }
    }

    private fun requestSystemAlertWindowPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK // Add this line
            startActivity(intent)
        }
    }
}

@Composable
fun WidgetContent() {
    // Define the UI for your widget using Jetpack Compose
    Column(modifier = Modifier.fillMaxSize() ,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,) {
        Text(text = "This is a full-screen widget", modifier = Modifier.fillMaxWidth())

        Button(onClick = {/* hideWidget()*/ }) {
            Text(text = "Close Widget")
        }
    }
}