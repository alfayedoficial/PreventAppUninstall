package com.casecode.preventUninstallApp.service.usecase

import android.view.accessibility.AccessibilityNodeInfo
import com.casecode.preventUninstallApp.service.utils.ServiceUtils

/**
 * AppNameUseCase is a class responsible for checking the presence of the app name within the accessibility tree.
 */
class AppNameUseCase {

    /**
     * Checks if the app name is present within the accessibility tree rooted at rootNode.
     *
     * @param rootNode The root node of the accessibility tree to search.
     * @return true if the app name is found, false otherwise.
     */
    operator fun invoke(rootNode: AccessibilityNodeInfo): Boolean {
        // Find the node containing the app name in English
        val forceStopButtonNode = ServiceUtils.findNodeByText(rootNode, APP_NAME_ENGLISH)
        return forceStopButtonNode != null
    }

    companion object {
        // Constants for app name text in English and Arabic
        private const val APP_NAME_ENGLISH = "MyDeviceAdminReceiver"
        private const val APP_NAME_ARABIC = "MyDeviceAdminReceiver"
    }
}