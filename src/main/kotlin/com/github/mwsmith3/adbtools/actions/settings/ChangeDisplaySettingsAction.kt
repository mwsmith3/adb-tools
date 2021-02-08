package com.github.mwsmith3.adbtools.actions.settings

import com.github.mwsmith3.adbtools.actions.AdbOptionsAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.settings.ChangeDisplaySettingsCommand
import com.github.mwsmith3.adbtools.command.settings.Display
import com.intellij.openapi.actionSystem.AnActionEvent

class ChangeDisplaySettingsAction : AdbOptionsAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val project = event.project
        if (project != null && device != null) {
            showOptions(event, "Display Options", Display.LIST) {
                execute(
                    Runnable {
                        CommandRunner.run(device, ChangeDisplaySettingsCommand(it))
                    }
                )
            }
        }
    }
}
