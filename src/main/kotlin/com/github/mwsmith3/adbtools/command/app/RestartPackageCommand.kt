package com.github.mwsmith3.adbtools.command.app

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.github.mwsmith3.adbtools.command.Command
import java.util.concurrent.TimeUnit

class RestartPackageCommand(private val packageName: String, private val activityName: String) : Command<Unit> {
    override val command: String
        get() = "am force-stop $packageName && am start $packageName/$activityName"
    override val description: String
        get() = "Restart package"

    override fun run(device: IDevice) {
        device.executeShellCommand(command, GenericReceiver(), 15L, TimeUnit.SECONDS)
    }
}