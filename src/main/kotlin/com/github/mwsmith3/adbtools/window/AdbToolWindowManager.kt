package com.github.mwsmith3.adbtools.window

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.device.DeviceListener
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager

class AdbToolWindowManager(private val project: Project) : DeviceListener {

    override fun onDevicesChanged(devices: List<IDevice>) {
        Logger.getInstance(AdbToolWindowManager::class.java).info("Devices changed: $devices")
        val contents = AdbToolWindowFactory.getContents(devices)
        val contentManager = contentManager()
        contentManager?.removeAllContents(true)
        contents.forEach {
            contentManager?.addContent(it)
        }
    }

    private fun contentManager() = ToolWindowManager.getInstance(project).getToolWindow("adb")?.contentManager
}