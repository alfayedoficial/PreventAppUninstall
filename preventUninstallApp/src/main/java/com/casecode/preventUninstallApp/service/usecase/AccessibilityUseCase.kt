package com.casecode.preventUninstallApp.service.usecase

import android.view.accessibility.AccessibilityNodeInfo
import com.casecode.preventUninstallApp.service.utils.ServiceUtils.findNodeByText

/**
 * AccessibilityUseCase is a class responsible for performing accessibility-related checks.
 */
class AccessibilityUseCase {

    /**
     * Checks the accessibility state based on the provided AccessibilityNodeInfo.
     *
     * @param rootNode The root node of the accessibility tree.
     * @return true if accessibility criteria are met, false otherwise.
     */
    operator fun invoke(rootNode: AccessibilityNodeInfo?): Boolean {

        // Check if the root node is null
        if (rootNode == null) return false

        // Find accessibility nodes for English and Arabic
        val accessibilityNodeEnglish = findNodeByText(rootNode, ACCESSIBILITY_ENGLISH)
        val accessibilityNodeArabic = findNodeByText(rootNode, ACCESSIBILITY_ARABIC)

        // Find 'On' nodes for English and Arabic
        val onNodeEnglish = findNodeByText(rootNode, ON_ENGLISH)
        val onNodeArabic = findNodeByText(rootNode, ON_ARABIC)

        // Check if app name is present and at least one accessibility node and one 'On' node is found
        return AppNameUseCase().invoke(rootNode)
                && (accessibilityNodeEnglish != null || accessibilityNodeArabic != null)
                && (onNodeEnglish != null || onNodeArabic != null)
    }

    companion object {
        // Constants for accessibility text in English and Arabic
        private const val ACCESSIBILITY_ENGLISH = "Accessibility"
        private const val ACCESSIBILITY_ARABIC = "سهولة الاستخدام"

        // Constants for 'On' text in English and Arabic
        private const val ON_ENGLISH = "On"
        private const val ON_ARABIC = "مفعّل"
    }
}