package com.github.mwsmith3.adbtools.actions.app

import com.android.tools.idea.run.activity.ActivityLocator
import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.Result
import com.github.mwsmith3.adbtools.command.app.RestartPackageCommand
import com.github.mwsmith3.adbtools.util.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent

class RestartAction : AdbAction() {

    @Suppress("ReturnCount")
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event) ?: return
        val packageName = getPackageName(event) ?: return
        val facet = getFacet(event) ?: return
        val project = event.project

        execute {
            try {
                val activityName = getDefaultActivityName(facet, device)
                val result = CommandRunner.run(
                    device,
                    RestartPackageCommand(packageName, activityName)
                )
                if (result is Result.Error) {
                    Notifications.error("adb command executed with errors: $result.message", project)
                }
            } catch (e: ActivityLocator.ActivityLocatorException) {
                Notifications.error("Unable to locate default activity for package $packageName", project)
            }
        }
    }
}
