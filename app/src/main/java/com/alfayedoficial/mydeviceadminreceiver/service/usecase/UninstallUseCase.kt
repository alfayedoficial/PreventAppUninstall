package com.alfayedoficial.mydeviceadminreceiver.service.usecase

import android.view.accessibility.AccessibilityNodeInfo
import com.alfayedoficial.mydeviceadminreceiver.service.utils.ServiceUtils.findNodeByText

/**
 * UninstallUseCase is a class responsible for managing uninstallation-related tasks.
 */
class UninstallUseCase {

    /**
     * Checks the presence of uninstallation confirmation nodes based on the provided AccessibilityNodeInfo.
     *
     * @param rootNode The root node of the accessibility tree.
     * @return true if uninstallation confirmation nodes are found, false otherwise.
     */
    operator fun invoke(rootNode: AccessibilityNodeInfo?): Boolean {

        // Check if the root node is null
        if (rootNode == null) return false

        // Find uninstallation confirmation nodes for English and Arabic
        val uninstallNodeEnglish = findNodeByText(rootNode, UNINSTALL_ENGLISH)
        val uninstallNodeArabic = findNodeByText(rootNode, UNINSTALL_ARABIC)

        // Check if app name is present and at least one uninstallation confirmation node is found
        return AppNameUseCase().invoke(rootNode) && (uninstallNodeEnglish != null || uninstallNodeArabic != null)
    }

    companion object {
        // Constants for uninstallation confirmation text in English and Arabic
        private const val UNINSTALL_ENGLISH = "Do you want to uninstall this app?"
        private const val UNINSTALL_ARABIC = "هل تريد إزالة هذا التطبيق"
    }
}