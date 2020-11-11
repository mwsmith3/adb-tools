package com.github.mwsmith3.adbtools.actions.adbexecutable

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.listeners.publishAdbExecutable
import com.github.mwsmith3.adbtools.requests.GetTalkBackInstalledRequest
import com.github.mwsmith3.adbtools.requests.Request
import com.github.mwsmith3.adbtools.requests.TalkBackGooglePlayRequest
import com.github.mwsmith3.adbtools.requests.ToggleTalkBackRequest
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object TalkBackAdbExecutable : AdbExecutable {
    private val TALKBACK_NOTIFICATION_GROUP = NotificationGroup("TalkBack Install", NotificationDisplayType.STICKY_BALLOON, true, null, null)

    override fun execute(project: Project, device: IDevice) {
        val talkBackInstalledRequestResponse = GetTalkBackInstalledRequest().call(project, device)
        if (talkBackInstalledRequestResponse is Request.Response.Success) {
            if (talkBackInstalledRequestResponse.response) {
                ToggleTalkBackRequest().call(project, device)
            } else {
                val notification = TALKBACK_NOTIFICATION_GROUP.createNotification("TalkBack not installed", "Do you want to install TalkBack?", NotificationType.ERROR)
                notification.addAction(
                        NotificationAction.createSimple("Go to Google Play") {
                            project.publishAdbExecutable(device, object : AdbExecutable {
                                override fun execute(project: Project, device: IDevice) {
                                    TalkBackGooglePlayRequest().call(project, device)
                                    notification.expire()
                                }
                            })
                        }
                )
                notification.notify(project)
            }
        }
    }
}