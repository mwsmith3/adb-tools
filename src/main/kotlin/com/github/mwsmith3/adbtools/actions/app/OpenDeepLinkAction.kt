package com.github.mwsmith3.adbtools.actions.app

import com.github.mwsmith3.adbtools.actions.AdbAction
import com.github.mwsmith3.adbtools.command.CommandRunner
import com.github.mwsmith3.adbtools.command.OpenDeepLinkCommand
import com.github.mwsmith3.adbtools.window.AdbToolsWindowView.Companion.DEEP_LINK_KEY
import com.intellij.openapi.actionSystem.AnActionEvent

class OpenDeepLinkAction : AdbAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val deepLink = event.getData(DEEP_LINK_KEY)
        val project = event.project
        val packageName = getPackageName(event)
        if (device != null && deepLink != null && project != null && packageName != null) {
            execute(project) { CommandRunner.run(device, OpenDeepLinkCommand(project, packageName, deepLink)) }
        }
    }
}