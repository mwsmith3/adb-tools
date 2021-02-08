package com.github.mwsmith3.adbtools.actions.settings

import com.github.mwsmith3.adbtools.actions.AdbOptionsAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.settings.AnimationScale
import com.github.mwsmith3.adbtools.command.settings.SetAnimationScalesCommand
import com.intellij.openapi.actionSystem.AnActionEvent

class SetAnimationScalesAction : AdbOptionsAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val project = event.project
        if (project != null && device != null) {
            showOptions(event, "Animation Scale Options", AnimationScale.LIST) {
                execute(Runnable {
                    CommandRunner.run(device, SetAnimationScalesCommand(it))
                })
            }
        }
    }
}
