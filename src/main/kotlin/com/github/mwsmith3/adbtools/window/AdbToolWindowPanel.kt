package com.github.mwsmith3.adbtools.window

import com.android.ddmlib.IDevice
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.DataKey
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
        val actionGroup = actionManager.getAction("com.github.mwsmith3.adbtools.window.actions") as DefaultActionGroup
        val actionToolbar = actionManager.createActionToolbar(ActionPlaces.TOOLBAR, actionGroup, true)
        this.toolbar = actionToolbar.component
        actionToolbar.setTargetComponent(this)
        this.setContent(windowContent)
    }

    override fun getData(dataId: String): Any? {
        return if (DEVICE_KEY.`is`(dataId)) {
            device
        } else {
            super.getData(dataId)
        }
    }

    companion object {
        val DEVICE_KEY = DataKey.create<IDevice>("device")
    }
}