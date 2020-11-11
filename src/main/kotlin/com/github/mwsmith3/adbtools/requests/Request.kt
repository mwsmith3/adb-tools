package com.github.mwsmith3.adbtools.requests

import com.android.ddmlib.AdbCommandRejectedException
import com.android.ddmlib.IDevice
import com.android.ddmlib.ShellCommandUnresponsiveException
import com.android.ddmlib.TimeoutException
import com.github.mwsmith3.adbtools.util.NotificationHelper
import com.intellij.openapi.project.Project
import kotlinx.coroutines.runBlocking

abstract class Request<T> {
    // TODO check generics
    // TODO Change the response to handle something like a Resource

    fun call(project: Project, device: IDevice): Response<T> {
        return try {
            runBlocking {
                val response = execute(project, device)
                Response.Success(response)
            }
        } catch (e: Exception) {
            val reason = when (e) {
                is TimeoutException -> "timeout"
                is AdbCommandRejectedException -> "adb command rejected"
                is ShellCommandUnresponsiveException -> "adb shell unresponsive"
                else -> "unknown"
            }
            NotificationHelper.error("$requestDescription failed. ($reason)")
            Response.Error(e)
        }
    }

    abstract val requestDescription: String

    abstract fun execute(project: Project, device: IDevice): T

    sealed class Response<out T> {
        data class Success<T>(val response: T) : Response<T>()
        data class Error(val error: Throwable) : Response<Nothing>()
    }
}