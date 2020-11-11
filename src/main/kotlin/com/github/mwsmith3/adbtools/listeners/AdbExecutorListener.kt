package com.github.mwsmith3.adbtools.listeners

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.actions.adbexecutable.AdbExecutable
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AdbExecutorListener(private val project: Project) : AdbExecutor, ProjectManagerListener, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val jobs = mutableSetOf<Job>()

    override fun execute(device: IDevice, adbExecutable: AdbExecutable) {
        val job = launch(coroutineContext) {
            adbExecutable.execute(project, device)
        }
        jobs += job
        job.invokeOnCompletion {
            jobs -= job
        }
    }

    override fun projectClosed(project: Project) {
        super.projectClosed(project)
        jobs.forEach { it.cancel() }
    }
}