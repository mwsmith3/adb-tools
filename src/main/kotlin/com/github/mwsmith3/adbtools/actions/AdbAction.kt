package com.github.mwsmith3.adbtools.actions

import com.github.mwsmith3.adbtools.window.AdbToolWindowPanel.Companion.DEVICE_KEY
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

abstract class AdbAction : AnAction() {
    fun getDevice(event: AnActionEvent) = event.getData(DEVICE_KEY)
}