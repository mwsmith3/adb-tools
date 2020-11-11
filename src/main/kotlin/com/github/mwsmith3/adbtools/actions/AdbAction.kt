package com.github.mwsmith3.adbtools.actions

import com.github.mwsmith3.adbtools.actions.adbexecutable.AdbExecutable
import com.github.mwsmith3.adbtools.listeners.publishAdbExecutable
import com.github.mwsmith3.adbtools.window.AdbToolWindowPanel.Companion.DEVICE_KEY
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

abstract class AdbAction : AnAction() {

    // TODO change icon programatically to be a loading

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        val device = event.getData(DEVICE_KEY)

        if (project != null && device != null) {
            project.publishAdbExecutable(device, getAdbExecutable())
        }
    }

    abstract fun getAdbExecutable(): AdbExecutable
}