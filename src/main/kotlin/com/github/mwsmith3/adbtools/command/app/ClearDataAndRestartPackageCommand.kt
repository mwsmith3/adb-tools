package com.github.mwsmith3.adbtools.command.app

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.github.mwsmith3.adbtools.command.Command
import java.util.concurrent.TimeUnit

class ClearDataAndRestartPackageCommand(private val packageName: String, private val activityName: String) : Command<Unit> {
    override val command: String
        get() = "pm clear $packageName && am start $packageName/$activityName"
    override val description: String
        get() = "Clear app data and restart"

    override fun run(device: IDevice) {
        device.executeShellCommand(command, GenericReceiver(), 15L, TimeUnit.SECONDS)
    }
}
