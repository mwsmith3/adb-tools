package com.github.mwsmith3.adbtools.window

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.device.DeviceProviderService
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory

class AdbToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val deviceProvider = ApplicationManager.getApplication().getService(DeviceProviderService::class.java)
        val devices = deviceProvider.devices
        val contents = getContents(devices, project)
        contents.forEach {
            toolWindow.contentManager.addContent(it)
        }
    }

    companion object {
        fun getContents(devices: List<IDevice>, project: Project): List<Content> {
            val contentFactory = ContentFactory.SERVICE.getInstance()
            return if (devices.isEmpty()) {
                listOf(contentFactory.createContent(AdbToolWindow.emptyContent, "", false))
            } else {
                devices.map {
                    val panel = AdbToolWindowPanel(it, project)
                    contentFactory.createContent(panel, it.serialNumber, false)
                }
            }
        }
    }
}