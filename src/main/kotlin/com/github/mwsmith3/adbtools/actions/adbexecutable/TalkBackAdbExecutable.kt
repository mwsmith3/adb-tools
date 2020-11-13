package com.github.mwsmith3.adbtools.actions.adbexecutable

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.publishAdbExecutable
import com.github.mwsmith3.adbtools.command.GetPackageInstalledCommand
import com.github.mwsmith3.adbtools.command.talkback.GetTalkBackEnabledCommand
import com.github.mwsmith3.adbtools.command.talkback.GoToTalkBackGooglePlayCommand
import com.github.mwsmith3.adbtools.command.talkback.ToggleTalkBackCommand
import com.github.mwsmith3.adbtools.requests.*
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object TalkBackAdbExecutable : AdbExecutable {
    private val TALKBACK_NOTIFICATION_GROUP = NotificationGroup("TalkBack Install", NotificationDisplayType.STICKY_BALLOON, true, null, null)

    override fun execute(project: Project, device: IDevice) {
        val talkBackInstalled = CommandRunner.run(device, GetPackageInstalledCommand("com.google.android.marvin.talkback"))
        if (talkBackInstalled) {
            val talkBackEnabled = CommandRunner.run(device, GetTalkBackEnabledCommand)
            CommandRunner.run(device, ToggleTalkBackCommand(talkBackEnabled))
        } else {
            val notification = TALKBACK_NOTIFICATION_GROUP.createNotification("TalkBack not installed", "Do you want to install TalkBack?", NotificationType.ERROR)
            notification.addAction(
                    NotificationAction.createSimple("Go to Google Play") {
                        project.publishAdbExecutable(device, object : AdbExecutable {
                            override fun execute(project: Project, device: IDevice) {
                                CommandRunner.run(device, GoToTalkBackGooglePlayCommand)
                                notification.expire()
                            }
                        })
                    }
            )
            notification.notify(project)
        }
    }
}