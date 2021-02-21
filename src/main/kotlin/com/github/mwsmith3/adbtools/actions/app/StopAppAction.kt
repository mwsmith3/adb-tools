package com.github.mwsmith3.adbtools.actions.app

import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.app.ForceStopAppCommand
import com.intellij.openapi.actionSystem.AnActionEvent

class StopAppAction : AdbAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val project = event.project
        val packageName = getPackageName(event)

        if (project != null && device != null && packageName != null) {
            execute {
                CommandRunner.run(device, ForceStopAppCommand(packageName))
            }
        }
    }
}
