package com.github.mwsmith3.adbtools.actions.app

import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.app.KillPackageCommand
import com.intellij.openapi.actionSystem.AnActionEvent

class KillAction : AdbAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val project = event.project
        val packageName = getPackageName(event)

        if (project != null && device != null && packageName != null) {
            execute(
                Runnable {
                    CommandRunner.run(device, KillPackageCommand(packageName))
                }
            )
        }
    }
}
