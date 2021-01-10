package com.github.mwsmith3.adbtools.command

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.github.mwsmith3.adbtools.command.Command.Companion.TIMEOUT
import java.util.concurrent.TimeUnit

abstract class StartForResultCommand(protected val packageName: String) : Command<Result> {

    private val shellOutputPredicate: (String) -> Boolean = {
        it.contains(Regex("(?i)(error)")) || it.contains(Regex("(?i)fail"))
    }

    override fun run(device: IDevice): Result {
        val receiver = GenericReceiver()
        device.executeShellCommand(command, receiver, TIMEOUT, TimeUnit.SECONDS)

        val isError =
            receiver.shellOutput.find(shellOutputPredicate) != null
        return if (isError) {
            Result.Error(
                receiver.shellOutput.joinToString(separator = "\n") {
                    it
                }
            )
        } else {
            Result.Success
        }
    }
}
