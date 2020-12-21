package com.github.mwsmith3.adbtools.command

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.github.mwsmith3.adbtools.util.Debugger
import com.intellij.openapi.project.Project
import java.util.concurrent.TimeUnit

abstract class StartForResultCommand(
    private val attachDebugger: Boolean,
    protected val packageName: String,
    private val project: Project
) : Command<Result> {

    override fun run(device: IDevice): Result {
        val receiver = GenericReceiver()
        device.executeShellCommand(command, receiver, 15L, TimeUnit.SECONDS)
        if (attachDebugger) {
            Debugger(project, device, packageName).attach()
        }

        val isError = receiver.shellOutput.find { it.contains(Regex("(?i)(error)")) || it.contains(Regex("(?i)fail")) } != null
        return if (isError) {
            Result.Error(receiver.shellOutput.joinToString(separator = "\n") {
                it
            })
        } else {
            Result.Success
        }
    }
}