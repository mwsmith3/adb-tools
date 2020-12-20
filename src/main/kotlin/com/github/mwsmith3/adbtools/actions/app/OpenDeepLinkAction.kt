package com.github.mwsmith3.adbtools.actions.app

import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.OpenDeepLinkCommand
import com.github.mwsmith3.adbtools.command.Result
import com.github.mwsmith3.adbtools.util.NotificationHelper
import com.github.mwsmith3.adbtools.window.AdbToolsWindowView
import com.github.mwsmith3.adbtools.window.AdbToolsWindowView.Companion.DEEP_LINK_KEY
import com.intellij.openapi.actionSystem.AnActionEvent

class OpenDeepLinkAction : AdbAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event) ?: return
        val deepLink = event.getData(DEEP_LINK_KEY) ?: return
        val project = event.project ?: return
        val packageName = getPackageName(event) ?: return

        execute(project) {
            val result = CommandRunner.run(
                device,
                OpenDeepLinkCommand(getAttachDebugger(event), packageName, project, deepLink)
            )
            if (result is Result.Error) {
                NotificationHelper.error("Unable to start deep link: \n\n${result.message}")
            }
        }
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabled = e.getData(AdbToolsWindowView.DEVICE_KEY) != null
                && e.getData(DEEP_LINK_KEY) != null
    }
}