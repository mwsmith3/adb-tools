package com.github.mwsmith3.adbtools.actions.app

import com.android.tools.idea.run.activity.ActivityLocator
import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.Result
import com.github.mwsmith3.adbtools.command.app.StartPackageCommand
import com.github.mwsmith3.adbtools.util.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent

class StartDefaultActivityAction : AdbAction() {

    @Suppress("ReturnCount")
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event) ?: return
        val packageName = getPackageName(event) ?: return
        val project = event.project
        val facet = getFacet(event) ?: return

        try {
            val activityName = getDefaultActivityName(facet, device)
            execute {
                val result = CommandRunner.run(
                    device,
                    StartPackageCommand(
                        packageName,
                        activityName
                    )
                )
                if (result is Result.Error) {
                    Notifications.error("adb command executed with errors: $result.message", project)
                }
            }
        } catch (e: ActivityLocator.ActivityLocatorException) {
            Notifications.error("Unable to locate default activity for package $packageName", project)
        }
    }
}
