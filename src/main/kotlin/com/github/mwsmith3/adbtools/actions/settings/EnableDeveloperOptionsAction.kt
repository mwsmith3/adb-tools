package com.github.mwsmith3.adbtools.actions.settings

import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.settings.EnableDeveloperOptionsCommand
import com.intellij.openapi.actionSystem.AnActionEvent

class EnableDeveloperOptionsAction : AdbAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val project = event.project

        if (project != null && device != null) {
            execute {
                CommandRunner.run(device, EnableDeveloperOptionsCommand())
            }
        }
    }
}
