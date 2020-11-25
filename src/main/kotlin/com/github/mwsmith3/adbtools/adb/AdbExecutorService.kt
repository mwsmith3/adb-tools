package com.github.mwsmith3.adbtools.adb

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AdbExecutorService(private val project: Project) : Disposable, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val jobs = mutableSetOf<Job>()

    fun execute(executable: () -> (Unit)) {
        val job = launch(coroutineContext) {
            executable()
        }
        jobs += job
        job.invokeOnCompletion {
            jobs -= job
        }
    }

    override fun dispose() {
        jobs.forEach { it.cancel() }
    }
}