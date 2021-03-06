package com.github.mwsmith3.adbtools.command

import com.android.ddmlib.AdbCommandRejectedException
import com.android.ddmlib.IDevice
import com.android.ddmlib.ShellCommandUnresponsiveException
import com.android.ddmlib.TimeoutException
import com.github.mwsmith3.adbtools.util.Notifications
import java.io.IOException

@Suppress("TooGenericExceptionCaught", "ComplexCondition")
object CommandRunner {

    @Throws(
        TimeoutException::class,
        AdbCommandRejectedException::class,
        ShellCommandUnresponsiveException::class,
        IOException::class
    )
    fun <T> run(device: IDevice, command: Command<T>): T {
        Notifications.info("${device.name}: ${command.adbCommand}")
        return try {
            command.run(device)
        } catch (e: Exception) {
            if (e is TimeoutException ||
                e is AdbCommandRejectedException ||
                e is ShellCommandUnresponsiveException ||
                e is IOException
            ) {
                Notifications.error("${command.description} failed. (${e.message})")
            }
            throw e
        }
    }
}
