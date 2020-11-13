package com.github.mwsmith3.adbtools.actions

import com.github.mwsmith3.adbtools.actions.adbexecutable.TalkBackAdbExecutable
import com.github.mwsmith3.adbtools.adb.publishAdbExecutable
import com.intellij.openapi.actionSystem.AnActionEvent

class TalkbackAction : AdbAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val device = getDevice(event)
        val project = event.project

        if (project != null && device != null) {
            project.publishAdbExecutable(device, TalkBackAdbExecutable)
        }
    }
}