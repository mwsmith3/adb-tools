package com.github.mwsmith3.adbtools.window

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class AdbToolsWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val model = AdbToolsModel()
        val view = AdbToolsWindowView(model)
        AdbToolsController(project, model)

        val contentFactory = toolWindow.contentManager.factory
        val content = contentFactory.createContent(view, "", false)
        toolWindow.contentManager.addContent(content)
    }
}
