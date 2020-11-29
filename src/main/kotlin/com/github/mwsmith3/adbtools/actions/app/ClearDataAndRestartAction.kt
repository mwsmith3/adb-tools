package com.github.mwsmith3.adbtools.actions.app

import com.android.tools.idea.run.activity.ActivityLocator
import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.app.ClearDataAndRestartPackageCommand
import com.github.mwsmith3.adbtools.util.NotificationHelper
import com.github.mwsmith3.adbtools.util.getDefaultActivityName
import com.intellij.openapi.actionSystem.AnActionEvent

class ClearDataAndRestartAction : AdbAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val project = event.project
        val packageName = getPackageName(event)
        val facet = getFacet(event)

        if (project != null && device != null && packageName != null && facet != null) {
            execute(project) {
                try {
                    val activityName = getDefaultActivityName(facet, device)
                    CommandRunner.run(device, ClearDataAndRestartPackageCommand(getAttachDebugger(event), packageName, activityName, project))
                } catch (e: ActivityLocator.ActivityLocatorException) {
                    NotificationHelper.error("Unable to locate default activity for package $packageName")
                }
            }
        }
    }
}