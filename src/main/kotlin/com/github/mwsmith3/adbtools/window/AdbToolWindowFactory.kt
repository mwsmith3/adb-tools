package com.github.mwsmith3.adbtools.window

import com.github.mwsmith3.adbtools.device.DeviceProviderService
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class AdbToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        Logger.getInstance(AdbToolWindowFactory::class.java).info("creating tool window content for project: $project")
        val deviceRepository = getDeviceRepositoryService(project)
        val model = AdbToolsModel(project)
        deviceRepository.addListener(model)
//        val controller = AdbToolWindowManager(project, model)
        val view = AdbToolWindowPanel(project, model)

        Logger.getInstance(AdbToolWindowFactory::class.java).info("created view: ${System.identityHashCode(view)}")
//        val devices = deviceRepository.devices
//        val contents = getContents(devices, project)
        val contentFactory = toolWindow.contentManager.factory
        val content = contentFactory.createContent(view, "", false)
        toolWindow.contentManager.addContent(content)
    }

    private fun getDeviceRepositoryService(project: Project): DeviceProviderService {
        return project.getService(DeviceProviderService::class.java)
    }
}