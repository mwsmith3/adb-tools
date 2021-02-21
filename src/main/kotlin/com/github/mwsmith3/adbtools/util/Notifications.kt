package com.github.mwsmith3.adbtools.util

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object Notifications {
    private const val NOTIFICATION_TITLE = "ADB Tools"
    val INFO_GROUP = NotificationGroup("ADB Logs", NotificationDisplayType.NONE, true, null, null)
    val ERROR_GROUP = NotificationGroup("ADB Error", NotificationDisplayType.BALLOON, true, null, null)
    val STICKY_GROUP =
        NotificationGroup("ADB Warning", NotificationDisplayType.STICKY_BALLOON, true, null, null)

    fun info(message: String, project: Project? = null) {
        return INFO_GROUP.createNotification(
            NOTIFICATION_TITLE,
            message,
            NotificationType.INFORMATION,
            null
        ).notify(project)
    }

    fun error(message: String, project: Project? = null) {
        return ERROR_GROUP.createNotification(
            NOTIFICATION_TITLE,
            message,
            NotificationType.ERROR,
            null
        ).notify(project)
    }
}
