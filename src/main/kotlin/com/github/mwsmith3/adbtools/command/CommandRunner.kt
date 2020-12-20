package com.github.mwsmith3.adbtools.command

import com.android.ddmlib.AdbCommandRejectedException
import com.android.ddmlib.IDevice
import com.android.ddmlib.ShellCommandUnresponsiveException
import com.android.ddmlib.TimeoutException
import com.github.mwsmith3.adbtools.util.NotificationHelper
import kotlinx.coroutines.runBlocking
import java.io.IOException

object CommandRunner {

    @Throws(TimeoutException::class, AdbCommandRejectedException::class, ShellCommandUnresponsiveException::class, IOException::class)
    fun <T> run(device: IDevice, command: Command<T>): T {
        NotificationHelper.info("${device.name}: ${command.command}")
        return try {
            runBlocking {
                command.run(device)
            }
        } catch (e: Exception) {
            if (e is TimeoutException || e is AdbCommandRejectedException || e is ShellCommandUnresponsiveException || e is IOException) {
                NotificationHelper.error("${command.description} failed. (${e.message})")
            }
            throw e
        }
    }
}