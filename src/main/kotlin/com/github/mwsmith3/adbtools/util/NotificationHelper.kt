package com.github.mwsmith3.adbtools.util

import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

/**
 * Taken from https://github.com/pbreault/adb-idea
 */
object NotificationHelper {
    private const val NOTIFICATION_TITLE = "Adb Tools"
    private val INFO = NotificationGroup("Adb Tools (Logging)", NotificationDisplayType.NONE, true, null, null)
    private val ERRORS = NotificationGroup("Adb Tools (Errors)", NotificationDisplayType.BALLOON, true, null, null)
    private val STICKY =
        NotificationGroup("Adb Tools (Sticky)", NotificationDisplayType.STICKY_BALLOON, true, null, null)

    fun info(message: String) {
        INFO.createNotification(NOTIFICATION_TITLE, escapeString(message), NotificationType.INFORMATION, null)
            .notify(null)
    }

    fun error(message: String) {
        ERRORS.createNotification(NOTIFICATION_TITLE, escapeString(message), NotificationType.ERROR, null).notify(null)
    }

    fun commandError(message: String) = error("adb command executed with errors: \n\n$message")

    fun confirmAction(project: Project, title: String, message: String, actionDescription: String, runnable: Runnable) {
        val notification = STICKY.createNotification(title, message, NotificationType.WARNING)
        val notificationAction = NotificationAction.createSimple(actionDescription) {
            runnable.run()
            notification.expire()
        }
        notification.addAction(notificationAction)
        notification.notify(project)
    }

    private fun escapeString(string: String) = string.replace("\n".toRegex(), "\n<br />")
}
