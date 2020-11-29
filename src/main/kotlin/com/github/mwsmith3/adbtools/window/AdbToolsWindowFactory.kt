package com.github.mwsmith3.adbtools.window

import com.github.mwsmith3.adbtools.device.DeviceProviderService
import com.github.mwsmith3.adbtools.device.DeviceProviderServiceImpl
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class AdbToolsWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val service = project.getService(DeviceProviderService::class.java)
        val model = AdbToolsModel(project)
        val view = AdbToolsWindowView(project, model)
        val controller = AdbToolsController(project, model, view, service)
        controller.setup()
        val contentFactory = toolWindow.contentManager.factory
        val content = contentFactory.createContent(view, "", false)
        toolWindow.contentManager.addContent(content)
    }
}