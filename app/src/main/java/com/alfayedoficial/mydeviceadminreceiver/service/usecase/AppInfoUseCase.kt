package com.alfayedoficial.mydeviceadminreceiver.service.usecase

import android.view.accessibility.AccessibilityNodeInfo
import com.alfayedoficial.mydeviceadminreceiver.service.utils.ServiceUtils.findNodeByText

/**
 * AppInfoUseCase is a class responsible for handling app information related tasks.
 */
class AppInfoUseCase {

    /**
     * Checks the presence of app information nodes based on the provided AccessibilityNodeInfo.
     *
     * @param rootNode The root node of the accessibility tree.
     * @return true if app information nodes are found, false otherwise.
     */
    operator fun invoke(rootNode: AccessibilityNodeInfo?): Boolean {

        // Check if the root node is null
        if (rootNode == null) return false

        // Find app information nodes for English and Arabic
        val appInfoNodeEnglish1 = findNodeByText(rootNode, APP_INFO_ENGLISH_1)
        val appInfoNodeEnglish2 = findNodeByText(rootNode, APP_INFO_ENGLISH_2)
        val appInfoNodeArabic1 = findNodeByText(rootNode, APP_INFO_ARABIC_1)
        val appInfoNodeArabic2 = findNodeByText(rootNode, APP_INFO_ARABIC_2)

        // Check if app name is present and at least one app information node is found
        return AppNameUseCase().invoke(rootNode) && (appInfoNodeEnglish1 != null || appInfoNodeEnglish2 != null || appInfoNodeArabic1 != null || appInfoNodeArabic2 != null)
    }

    companion object {
        // Constants for app information text in English and Arabic
        private const val APP_INFO_ENGLISH_1 = "App info"
        private const val APP_INFO_ENGLISH_2 = "App Info"
        private const val APP_INFO_ARABIC_1 = "معلومات التطبيق"
        private const val APP_INFO_ARABIC_2 = "معلومات عن التطبيقات"
    }
}