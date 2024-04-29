package com.casecode.preventUninstallApp.service.utils

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.AccessibilityNodeInfo
import com.casecode.preventUninstallApp.service.MyAccessibilityService
import java.util.LinkedList

/**
 * Utility functions related to accessibility services.
 */
object ServiceUtils {

    private const val TAG = "ServiceUtils"

    /**
     * Finds an AccessibilityNodeInfo containing the specified text within the accessibility tree rooted at rootNode.
     *
     * @param rootNode The root node of the accessibility tree to search.
     * @param searchText The text to search for within the accessibility tree.
     * @return The AccessibilityNodeInfo containing the specified text, or null if not found.
     */
    fun findNodeByText(
        rootNode: AccessibilityNodeInfo,
        searchText: String
    ): AccessibilityNodeInfo? {
        val nodeQueue = LinkedList<AccessibilityNodeInfo>()
        nodeQueue.add(rootNode)

        while (nodeQueue.isNotEmpty()) {
            val currentNode = nodeQueue.poll()
            if (currentNode != null && currentNode.text != null
                && currentNode.text.contains(searchText)
            ) {
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

    /**
     * Reads and logs all text content within the accessibility tree rooted at node.
     *
     * @param node The root node of the accessibility tree to read text from.
     */
    fun readAllText(node: AccessibilityNodeInfo?) {
        if (node == null) return

        // Check if the node has text and log/read it
        if (node.text != null && node.text.isNotEmpty()) {
            Log.d(TAG, "Text found: " + node.text)
        }

        // Recursively call this method for all children of this node
        for (i in 0 until node.childCount) {
            readAllText(node.getChild(i))
        }
    }

    /**
     * Checks if the accessibility service is enabled.
     *
     * @param context The context to access system services.
     * @return true if the accessibility service is enabled, false otherwise.
     */
    private fun isAccessibilityServiceEnabled(context: Context): Boolean {
        // Get the AccessibilityManager system service
        val accessibilityManager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

        // Get the list of enabled accessibility services with spoken feedback
        val enabledServices =
            accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN)

        // Create a ComponentName object for the MyAccessibilityService
        val serviceName = ComponentName(context, MyAccessibilityService::class.java)

        // Check if the service is in the list of enabled services
        for (service in enabledServices) {
            if (serviceName == ComponentName.unflattenFromString(service.id)) {
                return true
            }
        }
        return false
    }

    /**
     * Opens the accessibility settings activity.
     *
     * @param context The context to start the activity.
     */
    private fun openAccessibilitySettings(context: Context) {
        // Create an intent to open the accessibility settings
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)

        // Start the activity
        context.startActivity(intent)
    }

    /**
     * Checks if the accessibility service is enabled and prompts the user to enable it if not.
     *
     * @param context The context to access system services and start activities.
     */
    fun checkAndPromptAccessibilityService(context: Context) {
        // If the accessibility service is not enabled, prompt the user to enable it
        if (!isAccessibilityServiceEnabled(context)) {
            openAccessibilitySettings(context)
        }
    }
}