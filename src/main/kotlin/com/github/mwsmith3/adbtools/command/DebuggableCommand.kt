package com.github.mwsmith3.adbtools.command

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.github.mwsmith3.adbtools.util.Debugger
import com.intellij.openapi.project.Project
import java.util.concurrent.TimeUnit

abstract class DebuggableCommand(private val attachDebugger: Boolean, protected val packageName: String, private val project: Project) : Command<Unit> {

    override fun run(device: IDevice) {
        device.executeShellCommand(command, GenericReceiver(), 15L, TimeUnit.SECONDS)
        if (attachDebugger) {
            Debugger(project, device, packageName).attach()
        }
    }
}