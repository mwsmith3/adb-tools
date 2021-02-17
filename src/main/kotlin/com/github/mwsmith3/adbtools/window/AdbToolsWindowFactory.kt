package com.github.mwsmith3.adbtools.window

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import io.reactivex.rxjava3.subjects.BehaviorSubject

class AdbToolsWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val observableModel = BehaviorSubject.create<AdbToolsModel>()
        val view = AdbToolsWindowView(observableModel)
        AdbToolsController(project, view, observableModel)

        val contentFactory = toolWindow.contentManager.factory
        val content = contentFactory.createContent(view, "", false)
        toolWindow.contentManager.addContent(content)
    }
}
