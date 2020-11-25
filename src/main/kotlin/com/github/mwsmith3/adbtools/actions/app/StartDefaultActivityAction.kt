package com.github.mwsmith3.adbtools.actions.app

import com.android.tools.idea.run.activity.ActivityLocator
import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.GetPackageInstalledCommand
import com.github.mwsmith3.adbtools.command.app.StartPackageCommand
import com.github.mwsmith3.adbtools.util.NotificationHelper
import com.github.mwsmith3.adbtools.util.getDefaultActivityName
import com.intellij.openapi.actionSystem.AnActionEvent

class StartDefaultActivityAction : AdbAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val project = event.project
        val facet = getFacet(event)
        val packageName = getPackageName(event)

        if (project != null && device != null && facet != null && packageName != null) {
            execute(project) {
                val packageInstalled = CommandRunner.run(device, GetPackageInstalledCommand(packageName))
                if (packageInstalled) {
                    try {
                        val defaultActivityName = getDefaultActivityName(facet, device)
                        CommandRunner.run(device, StartPackageCommand(packageName, defaultActivityName))
                    } catch (e: ActivityLocator.ActivityLocatorException) {
                        NotificationHelper.error("Unable to start default activity on ${device.name}: ${e.message}")
                    }
                } else {
                    NotificationHelper.error("Package $packageName not installed on device $device")
                }
            }
        }
    }
}
