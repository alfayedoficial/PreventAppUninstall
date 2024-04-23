package com.alfayedoficial.mydeviceadminreceiver

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.util.LinkedList


const val ACTION = "com.alfayedoficial.mydeviceadminreceiver.PASSWORD_RESULT"
const val INFLATE_PASSWORD_ACTIVITY = "inflatedPasswordActivity"
const val MY_SHARED_PREF = "secret_shared_prefs"
const val COUNTER_ONE = "counterOne"
const val PASSWORD ="password"
class MyAccessibilityService : AccessibilityService() {


    private var counterOne = 0
    private val passwordResultReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ACTION) {
                val resultValue = intent.getIntExtra("result" , ServiceResult.FAILURE.value)
                counterOne = 0
                saveCounterOne(counterOne)
                if (resultValue == ServiceResult.SUCCESS.value) {
                    // Handle the success case
                    Log.d("TAG_Log", "Password entered successfully")
                    saveInflatedPasswordActivity(true)
                } else {
                    // Handle the failure case
                    Log.d("TAG_Log", "Password entry failed")
                    saveInflatedPasswordActivity(false)
                }
            }
        }
    }


    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter(ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(passwordResultReceiver, filter, RECEIVER_NOT_EXPORTED)
        }else{
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

            if ((accessibilityCase(rootNode) || appInfoCase(rootNode) || uninstallCase(rootNode)) && !getInflatedPasswordActivity() && getCounterOne() <1)  {
                // Check if the "App Info" screen for your app is opened
                inflatePasswordActivity()
            }
        }

        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            val rootNode = rootInActiveWindow
            rootNode?.let { readAllText(it) }
        }

    }

    private fun readAllText(node: AccessibilityNodeInfo?) {
        if (node == null) return

        // Check if the node has text and log/read it
        if (node.getText() != null && node.getText().isNotEmpty()) {
            Log.d("TextReader", "Text found: " + node.getText())
        }

        // Recursively call this method for all children of this node
        for (i in 0 until node.childCount) {
            readAllText(node.getChild(i))
        }
    }

    //TAG_Log

    private fun accessibilityCase(rootNode: AccessibilityNodeInfo?): Boolean {
        if (rootNode == null) return false
        val nodeAccessibility = findNodeByText(rootNode, "Accessibility")
        val nodeAccessibility_ar = findNodeByText(rootNode, "سهولة الاستخدام")
        val nodeOn = findNodeByText(rootNode, "On")
        val nodeOn_ar = findNodeByText(rootNode, "مفعّل")

        return hasAppName(rootNode) &&(nodeAccessibility != null || nodeAccessibility_ar != null) && (nodeOn != null || nodeOn_ar != null)
    }


    private fun appInfoCase(rootNode: AccessibilityNodeInfo?):Boolean{
        if (rootNode == null) return false
        val nodeAppInfo = findNodeByText(rootNode, "App Info")
        val nodeAppInfo_ar = findNodeByText(rootNode, "معلومات التطبيق")
        val nodeAppInfo_ar2 = findNodeByText(rootNode, "معلومات عن التطبيقات")

        return hasAppName(rootNode) &&(nodeAppInfo != null || nodeAppInfo_ar != null || nodeAppInfo_ar2 != null)
    }

    private fun uninstallCase(rootNode: AccessibilityNodeInfo?): Boolean{
        if (rootNode == null) return false
        val nodeUninstall = findNodeByText(rootNode, "Do you want to uninstall this app?")
        val nodeUninstall_ar = findNodeByText(rootNode, "هل تريد إزالة هذا التطبيق")

        return hasAppName(rootNode) &&(nodeUninstall != null || nodeUninstall_ar != null)
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


    private fun inflatePasswordActivity() {
        val passwordIntent = Intent(this, PasswordActivity::class.java)
        passwordIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(passwordIntent)
        logAppInfoScreenEvent()
        counterOne++
        saveCounterOne(counterOne)
    }


}