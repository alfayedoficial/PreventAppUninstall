package com.alfayedoficial.mydeviceadminreceiver.service.utils

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
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
}