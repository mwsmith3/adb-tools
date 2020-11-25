package com.github.mwsmith3.adbtools.actions.app

import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.GetPackageInstalledCommand
import com.github.mwsmith3.adbtools.command.app.KillPackageCommand
import com.github.mwsmith3.adbtools.util.NotificationHelper
import com.intellij.openapi.actionSystem.AnActionEvent

class KillAction : AdbAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val project = event.project
        val packageName = getPackageName(event)

        if (project != null && device != null && packageName != null) {
            execute(project) {
                val packageInstalled = CommandRunner.run(device, GetPackageInstalledCommand(packageName))
                if (packageInstalled) {
                    CommandRunner.run(device, KillPackageCommand(packageName))
                } else {
                    NotificationHelper.error("$packageName not installed on ${device.name}")
                }
            }
        }
    }
}