package com.github.mwsmith3.adbtools.command.settings

import com.android.ddmlib.IDevice
import com.github.mwsmith3.adbtools.adb.GenericReceiver
import com.github.mwsmith3.adbtools.command.Command
import java.util.concurrent.TimeUnit

object EnableDeveloperOptionsCommand : Command<Unit> {

    override val command: String
        get() = "settings put global development_settings_enabled 1"

    override val description: String
        get() = "Enable developer options"

    override fun run(device: IDevice) {
        device.executeShellCommand(command, GenericReceiver(), 15L, TimeUnit.SECONDS)
    }
}