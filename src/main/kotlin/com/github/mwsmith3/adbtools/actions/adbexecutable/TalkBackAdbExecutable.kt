package com.github.mwsmith3.adbtools.actions.adbexecutable

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.publishAdbExecutable
import com.github.mwsmith3.adbtools.requests.*
import com.github.mwsmith3.adbtools.requests.talkback.GetTalkBackEnabledRequest
import com.github.mwsmith3.adbtools.requests.talkback.GoToTalkBackGooglePlayRequest
import com.github.mwsmith3.adbtools.requests.talkback.ToggleTalkBackRequest
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object TalkBackAdbExecutable : AdbExecutable {
    private val TALKBACK_NOTIFICATION_GROUP = NotificationGroup("TalkBack Install", NotificationDisplayType.STICKY_BALLOON, true, null, null)

    override fun execute(project: Project, device: IDevice) {
        val talkBackInstalled = GetPackageInstalledRequest("com.google.android.marvin.talkback").call(project, device)
        if (talkBackInstalled) {
            val talkBackEnabled = GetTalkBackEnabledRequest().call(project, device)
            ToggleTalkBackRequest(talkBackEnabled).call(project, device)
        } else {
            val notification = TALKBACK_NOTIFICATION_GROUP.createNotification("TalkBack not installed", "Do you want to install TalkBack?", NotificationType.ERROR)
            notification.addAction(
                    NotificationAction.createSimple("Go to Google Play") {
                        project.publishAdbExecutable(device, object : AdbExecutable {
                            override fun execute(project: Project, device: IDevice) {
                                GoToTalkBackGooglePlayRequest().call(project, device)
                                notification.expire()
                            }
                        })
                    }
            )
            notification.notify(project)
        }
    }
}