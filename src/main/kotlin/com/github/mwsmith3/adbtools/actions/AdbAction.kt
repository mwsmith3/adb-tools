package com.github.mwsmith3.adbtools.actions

import com.android.tools.idea.gradle.project.model.AndroidModuleModel
import com.github.mwsmith3.adbtools.window.AdbToolsWindowView.Companion.DEVICE_KEY
import com.github.mwsmith3.adbtools.window.AdbToolsWindowView.Companion.FACET_KEY
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.jetbrains.ide.PooledThreadExecutor
import java.util.concurrent.ExecutorService

abstract class AdbAction : AnAction() {
    // TODO create app subclass that requires facet not to be null

    private val executor: ExecutorService = PooledThreadExecutor.INSTANCE

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

    fun execute(runnable: Runnable) {
        executor.execute(runnable)
    }
}
