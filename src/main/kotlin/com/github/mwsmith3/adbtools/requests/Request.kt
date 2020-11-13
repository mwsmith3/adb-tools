package com.github.mwsmith3.adbtools.requests

import com.android.ddmlib.AdbCommandRejectedException
import com.android.ddmlib.IDevice
import com.android.ddmlib.ShellCommandUnresponsiveException
import com.android.ddmlib.TimeoutException
import com.github.mwsmith3.adbtools.util.NotificationHelper
import com.intellij.openapi.project.Project
import kotlinx.coroutines.runBlocking
import java.io.IOException

abstract class Request<T> {
    // TODO check generics

    @Throws(TimeoutException::class, AdbCommandRejectedException::class, ShellCommandUnresponsiveException::class, IOException::class)
    fun call(project: Project, device: IDevice): T {
        NotificationHelper.info("${device.name}: $command")
        return try {
            runBlocking {
                execute(project, device)
            }
        } catch (e: Exception) {
            if (e is TimeoutException || e is AdbCommandRejectedException || e is ShellCommandUnresponsiveException || e is IOException) {
                NotificationHelper.error("$requestDescription failed. (${e.message})")
            }
            throw e
        }
    }

    abstract val requestDescription: String
    abstract val command: String

    abstract fun execute(project: Project, device: IDevice): T
}