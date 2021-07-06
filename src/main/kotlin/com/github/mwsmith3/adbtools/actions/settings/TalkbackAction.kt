package com.github.mwsmith3.adbtools.actions.settings

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.GetPackageInstalledCommand
import com.github.mwsmith3.adbtools.command.settings.GetTalkBackEnabledCommand
import com.github.mwsmith3.adbtools.command.settings.GoToTalkBackGooglePlayCommand
import com.github.mwsmith3.adbtools.command.settings.ToggleTalkBackCommand
import com.github.mwsmith3.adbtools.util.Notifications
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class TalkbackAction : AdbAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val project = event.project

        if (project != null && device != null) {
            execute {
                val talkBackInstalled =
                    CommandRunner.run(device, GetPackageInstalledCommand("com.google.android.marvin.talkback"))
                if (talkBackInstalled) {
                    val talkBackEnabled = CommandRunner.run(device, GetTalkBackEnabledCommand())
                    CommandRunner.run(device, ToggleTalkBackCommand(talkBackEnabled))
                } else {
                    showNotification(project, device)
                }
            }
        }
    }

    private fun showNotification(project: Project, device: IDevice) {
        val notification = Notifications.WARNING_GROUP.createNotification(
            "TalkBack not installed",
            "Do you want to install TalkBack?",
            NotificationType.WARNING
        )
        val notificationAction = NotificationAction.createSimple("Go to Google Play") {
            execute {
                CommandRunner.run(device, GoToTalkBackGooglePlayCommand())
            }
            notification.expire()
        }
        notification.addAction(notificationAction)
        notification.notify(project)
    }
}
