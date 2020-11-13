package com.github.mwsmith3.adbtools.actions

import com.github.mwsmith3.adbtools.actions.adbexecutable.OpenDeepLinkExecutable
import com.github.mwsmith3.adbtools.adb.publishAdbExecutable
import com.github.mwsmith3.adbtools.window.AdbToolWindowPanel.Companion.DEEP_LINK_KEY
import com.intellij.openapi.actionSystem.AnActionEvent

class OpenDeepLinkAction : AdbAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val deepLink = event.getData(DEEP_LINK_KEY)
        val project = event.project
        if (device != null && deepLink != null && project != null) {
            project.publishAdbExecutable(device, OpenDeepLinkExecutable(deepLink))
        }
    }
}