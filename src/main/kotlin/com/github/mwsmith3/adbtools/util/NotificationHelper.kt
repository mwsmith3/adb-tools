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
    private val INFO = NotificationGroup("Adb Tools (Logging)", NotificationDisplayType.NONE, true, null, null)
    private val ERRORS = NotificationGroup("Adb Tools (Errors)", NotificationDisplayType.BALLOON, true, null, null)
    private val STICKY = NotificationGroup("Adb Tools (Sticky)", NotificationDisplayType.STICKY_BALLOON, true, null, null)

    fun info(message: String) = sendNotification(message, NotificationType.INFORMATION, INFO)

    fun error(message: String) = sendNotification(message, NotificationType.ERROR, ERRORS)

    fun commandError(message: String) = sendNotification("adb command executed with errors: \n\n$message", NotificationType.ERROR, ERRORS)

    fun confirmAction(project: Project, title: String, message: String, actionDescription: String, runnable: Runnable) {
        val notification = STICKY.createNotification(title, message, NotificationType.WARNING)
        val notificationAction = NotificationAction.createSimple(actionDescription) {
            runnable.run()
            notification.expire()
        }
        notification.addAction(notificationAction)
        notification.notify(project)
    }

    private fun sendNotification(message: String, notificationType: NotificationType, notificationGroup: NotificationGroup) {
        notificationGroup.createNotification("ADB Tools", escapeString(message), notificationType, null).notify(null)
    }

    private fun escapeString(string: String) = string.replace("\n".toRegex(), "\n<br />")
}