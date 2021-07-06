package com.github.mwsmith3.adbtools.util

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object Notifications {
    private const val NOTIFICATION_TITLE = "ADB Tools"
    private val INFO_GROUP = NotificationGroup.logOnlyGroup("ADB Logs")
    private val ERROR_GROUP = NotificationGroup.balloonGroup("ADB Error")
    val WARNING_GROUP = NotificationGroup.balloonGroup("ADB Warning")

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
