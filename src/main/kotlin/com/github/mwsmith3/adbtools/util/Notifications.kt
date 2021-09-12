package com.github.mwsmith3.adbtools.util

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object Notifications {
    private const val NOTIFICATION_TITLE = "ADB Tools"
    private val LOG_GROUP = NotificationGroupManager.getInstance().getNotificationGroup("ADBTools Log")
    val POPUP_GROUP: NotificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("ADBTools Popup")

    fun info(message: String, project: Project? = null) {
        return LOG_GROUP.createNotification(
            NOTIFICATION_TITLE,
            message,
            NotificationType.INFORMATION,
            null
        ).notify(project)
    }

    fun error(message: String, project: Project? = null) {
        return POPUP_GROUP.createNotification(
            NOTIFICATION_TITLE,
            message,
            NotificationType.ERROR,
            null
        ).notify(project)
    }
}
