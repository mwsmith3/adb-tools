package com.github.mwsmith3.adbtools.ui

import com.android.ddmlib.IDevice
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.layout.panel

class AdbToolWindowPanel(private val device: IDevice) : SimpleToolWindowPanel(true, false) {

    // TODO add header
    // TODO fix icon

    private val windowContent = panel {
        row {
            label("a device!")
        }
    }

    init {
        val actionManager = ActionManager.getInstance()
        val actionGroup = actionManager.getAction("com.github.mwsmith3.adbtools.ADBActions") as DefaultActionGroup
        val actionToolbar = actionManager.createActionToolbar("ADB Action Toolbar", actionGroup, true)
        this.toolbar = actionToolbar.component
        this.setContent(windowContent)
    }
}