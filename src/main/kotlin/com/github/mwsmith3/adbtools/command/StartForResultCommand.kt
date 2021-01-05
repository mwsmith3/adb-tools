package com.github.mwsmith3.adbtools.command

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import java.util.concurrent.TimeUnit

abstract class StartForResultCommand(
    protected val packageName: String,
) : Command<Result> {

    override fun run(device: IDevice): Result {
        val receiver = GenericReceiver()
        device.executeShellCommand(command, receiver, 15L, TimeUnit.SECONDS)

        val isError = receiver.shellOutput.find { it.contains(Regex("(?i)(error)")) } != null
        return if (isError) {
            Result.Error(receiver.shellOutput.joinToString(separator = "\n") {
                it
            })
        } else {
            Result.Success
        }
    }
}