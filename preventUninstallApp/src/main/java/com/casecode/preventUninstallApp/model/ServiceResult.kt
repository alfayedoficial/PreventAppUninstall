package com.casecode.preventUninstallApp.model

/**
 * Enumeration representing the result of a service operation.
 * @property value The integer value associated with the service result.
 */
enum class ServiceResult(val value: Int) {
    /**
     * Represents a successful service operation.
     */
    SUCCESS(1),

    /**
     * Represents a failed service operation.
     */
    FAILURE(0)
}