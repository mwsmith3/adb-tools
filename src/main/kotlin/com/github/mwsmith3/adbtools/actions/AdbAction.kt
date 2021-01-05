package com.github.mwsmith3.adbtools.actions

import com.android.tools.idea.gradle.project.model.AndroidModuleModel
import com.github.mwsmith3.adbtools.adb.AdbExecutorService
import com.github.mwsmith3.adbtools.window.AdbToolsWindowView.Companion.DEVICE_KEY
import com.github.mwsmith3.adbtools.window.AdbToolsWindowView.Companion.FACET_KEY
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

abstract class AdbAction : AnAction() {
    // TODO create app subclass that requires facet not to be null

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabled = e.getData(DEVICE_KEY) != null
    }

    fun getDevice(event: AnActionEvent) = event.getData(DEVICE_KEY)

    fun getFacet(event: AnActionEvent) = event.getData(FACET_KEY)

    fun getPackageName(event: AnActionEvent): String? {
        val facet = getFacet(event)
        return facet?.let {
            AndroidModuleModel.get(facet)?.applicationId
        }
    }

    fun execute(project: Project, executable: () -> (Unit)) =
            project.getService(AdbExecutorService::class.java).execute(executable)
}
