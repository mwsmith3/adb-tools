package com.github.mwsmith3.adbtools.actions.settings

import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.GetPackageInstalledCommand
import com.github.mwsmith3.adbtools.command.settings.GetTalkBackEnabledCommand
import com.github.mwsmith3.adbtools.command.settings.GoToTalkBackGooglePlayCommand
import com.github.mwsmith3.adbtools.command.settings.ToggleTalkBackCommand
import com.github.mwsmith3.adbtools.util.NotificationHelper
import com.intellij.openapi.actionSystem.AnActionEvent

class TalkbackAction : AdbAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val project = event.project

        if (project != null && device != null) {
            execute(project) {
                val talkBackInstalled =
                    CommandRunner.run(device, GetPackageInstalledCommand("com.google.android.marvin.talkback"))
                if (talkBackInstalled) {
                    val talkBackEnabled = CommandRunner.run(device, GetTalkBackEnabledCommand)
                    CommandRunner.run(device, ToggleTalkBackCommand(talkBackEnabled))
                } else {
                    NotificationHelper.confirmAction(
                        project,
                        "TalkBack not installed",
                        "Do you want to install TalkBack?",
                        "Go to Google Play"
                    ) {
                        execute(project) {
                            CommandRunner.run(device, GoToTalkBackGooglePlayCommand)
                        }
                    }
                }
            }
        }
    }
}
