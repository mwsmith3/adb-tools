package com.github.mwsmith3.adbtools.actions

import com.android.ddmlib.IDevice
import com.android.tools.idea.gradle.project.model.AndroidModuleModel
import com.android.tools.idea.run.activity.ActivityLocator
import com.android.tools.idea.run.activity.DefaultActivityLocator
import com.github.mwsmith3.adbtools.window.AdbToolsWindowView.Companion.DEVICE_KEY
import com.github.mwsmith3.adbtools.window.AdbToolsWindowView.Companion.FACET_KEY
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.ThrowableComputable
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.ide.PooledThreadExecutor
import java.util.concurrent.ExecutorService

abstract class AdbAction : AnAction() {

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

    @Throws(ActivityLocator.ActivityLocatorException::class)
    fun getDefaultActivityName(facet: AndroidFacet, device: IDevice): String {
        return ApplicationManager.getApplication()
            .runReadAction(
                ThrowableComputable<String, ActivityLocator.ActivityLocatorException?> {
                    DefaultActivityLocator(
                        facet
                    ).getQualifiedActivityName(device)
                }
            )
    }

    fun execute(runnable: Runnable) {
        executor.execute(runnable)
    }
}
