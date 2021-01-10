package com.github.mwsmith3.adbtools.command.app

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.github.mwsmith3.adbtools.command.Command
import com.github.mwsmith3.adbtools.command.Command.Companion.TIMEOUT
import java.util.concurrent.TimeUnit

class KillPackageCommand(private val packageName: String) : Command<Unit> {
    override val command: String
        get() = "am force-stop $packageName"
    override val description: String
        get() = "Kill app"

    override fun run(device: IDevice) {
        device.executeShellCommand(command, GenericReceiver(), TIMEOUT, TimeUnit.SECONDS)
    }
}
