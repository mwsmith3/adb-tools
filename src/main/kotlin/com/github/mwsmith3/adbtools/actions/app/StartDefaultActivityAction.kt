package com.github.mwsmith3.adbtools.actions.app

import com.android.tools.idea.run.activity.ActivityLocator
import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.Result
import com.github.mwsmith3.adbtools.command.app.StartPackageCommand
import com.github.mwsmith3.adbtools.util.NotificationHelper
import com.github.mwsmith3.adbtools.util.getDefaultActivityName
import com.intellij.openapi.actionSystem.AnActionEvent

class StartDefaultActivityAction : AdbAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event) ?: return
        val project = event.project ?: return
        val packageName = getPackageName(event) ?: return
        val facet = getFacet(event) ?: return

        try {
            val activityName = getDefaultActivityName(facet, device)
            execute(project) {
                val result = CommandRunner.run(
                    device,
                    StartPackageCommand(
                        packageName,
                        activityName
                    )
                )
                if (result is Result.Error) {
                    NotificationHelper.error("Unable to start Activity: \n\n${result.message}")
                }
            }
        } catch (e: ActivityLocator.ActivityLocatorException) {
            NotificationHelper.error("Unable to locate default activity for package $packageName")
        }
    }
}
